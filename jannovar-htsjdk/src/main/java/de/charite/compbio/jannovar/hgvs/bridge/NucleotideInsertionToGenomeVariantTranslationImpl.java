package de.charite.compbio.jannovar.hgvs.bridge;

import de.charite.compbio.jannovar.hgvs.SequenceType;
import de.charite.compbio.jannovar.hgvs.nts.NucleotideRange;
import de.charite.compbio.jannovar.hgvs.nts.NucleotideSeqDescription;
import de.charite.compbio.jannovar.hgvs.nts.change.NucleotideInsertion;
import de.charite.compbio.jannovar.htsjdk.GenomeRegionSequenceExtractor;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.GenomeVariant;
import de.charite.compbio.jannovar.reference.Strand;
import de.charite.compbio.jannovar.reference.TranscriptModel;

/**
 * Implementation of converting nucleotide insertions to genome variants.
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@bihealth.de>
 */
class NucleotideInsertionToGenomeVariantTranslationImpl extends NucleotideChangeToGenomeVariantTranslationImplBase {

	public NucleotideInsertionToGenomeVariantTranslationImpl(GenomeRegionSequenceExtractor seqExtractor) {
		super(seqExtractor);
	}

	/**
	 * Implementation of translation for {@link NucleotideInsertion} objects
	 *
	 * @param tm
	 *            {@link TranscriptModel} that <code>ntSub</code> is for
	 * @param sequenceType
	 *            {@link SequenceType} that <code>ntSub</code> is for
	 * @param ntIns
	 *            {@link NucleotideInsertion} to convert
	 * @return {@link GenomeVariant} with the translation result, possibly annotated with warning messages
	 * @throws CannotTranslateHGVSVariant
	 *             in case of translation problems
	 */
	public ResultWithWarnings<GenomeVariant> run(TranscriptModel tm, SequenceType sequenceType,
			NucleotideInsertion ntIns) throws CannotTranslateHGVSVariant {
		final NucleotideRange range = ntIns.getRange();
		final NucleotideSeqDescription deletedNTDesc = ntIns.getSeq();
		final GenomeInterval gItv = posConverter.translateNucleotideRange(tm, range, sequenceType);

		// obtain deleted sequence, setting inconsistency warnings into warningMsg, if any
		String warningMsg = null;
		String deletedNTs = deletedNTDesc.getNucleotides();
		if (deletedNTs == null) {
			deletedNTs = getGenomeSeq(tm.getStrand(), gItv);
			if (deletedNTDesc.length() != NucleotideSeqDescription.INVALID_NT_COUNT
					&& deletedNTDesc.length() != deletedNTs.length())
				warningMsg = "Invalid nucleotide count in " + ntIns.toHGVSString() + ", expected "
						+ deletedNTs.length();
		} else {
			final String refSeq = getGenomeSeq(tm.getStrand(), gItv);
			if (!refSeq.equals(deletedNTs))
				warningMsg = "Invalid nucleotides in " + ntIns.toHGVSString() + ", expected " + refSeq;
			deletedNTs = refSeq;
		}

		final GenomeVariant result = new GenomeVariant(gItv.withStrand(tm.getStrand()).getGenomeBeginPos(), deletedNTs,
				"", tm.getStrand()).withStrand(Strand.FWD);
		if (warningMsg != null)
			return ResultWithWarnings.construct(result, warningMsg);
		else
			return ResultWithWarnings.construct(result);
	}

}
