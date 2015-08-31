package de.charite.compbio.jannovar.pedigree;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.charite.compbio.jannovar.pedigree.compatibilitychecker.CompatibilityCheckerException;
import de.charite.compbio.jannovar.pedigree.compatibilitychecker.ad.VariantContextCompatibilityCheckerAutosomalDominant;
import de.charite.compbio.jannovar.pedigree.compatibilitychecker.ar.VariantContextCompatibilityCheckerAutosomalRecessive;
import de.charite.compbio.jannovar.pedigree.compatibilitychecker.xd.CompatibilityCheckerXDominant;
import de.charite.compbio.jannovar.pedigree.compatibilitychecker.xr.CompatibilityCheckerXRecessive;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.GenotypeBuilder;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.VariantContextBuilder;

/** Base class for compatibility checkers with utility methods. */
public abstract class AbstractCompatibilityCheckerTest {

	protected final Genotype HET = Genotype.HETEROZYGOUS;
	protected final Genotype REF = Genotype.HOMOZYGOUS_REF;
	protected final Genotype ALT = Genotype.HOMOZYGOUS_ALT;
	protected final Genotype UKN = Genotype.NOT_OBSERVED;

	protected final Allele refAllele = Allele.create("A", true);
	protected final Allele altAllele = Allele.create("G", false);
	protected final Allele uknAllele = Allele.create(".", false);

	protected Pedigree pedigree;
	protected ImmutableList<String> names;

	String geneName = "bla";

	private List<VariantContext> getInheritanceVariantContextList(ImmutableList<Genotype> genotypes) {

		List<VariantContext> vcs = new ArrayList<VariantContext>();
		vcs.add(getVariantContext(genotypes, "chr1", 1));
		return vcs;
	}

	private List<VariantContext> getInheritanceVariantContextList(ImmutableList<Genotype> genotypes1,
			ImmutableList<Genotype> genotypes2) {

		List<VariantContext> vcs = new ArrayList<VariantContext>();
		vcs.add(getVariantContext(genotypes1, "chr1", 1));
		vcs.add(getVariantContext(genotypes2, "chr1", 2));
		return vcs;
	}

	private VariantContext getVariantContext(ImmutableList<Genotype> genotypes, String chr, int pos) {
		VariantContextBuilder vcBuilder = new VariantContextBuilder().chr(chr).start(pos).stop(pos)
				.alleles(ImmutableList.of(refAllele, altAllele));
		List<htsjdk.variant.variantcontext.Genotype> vcGenotypes = new ArrayList<htsjdk.variant.variantcontext.Genotype>(
				genotypes.size());
		int i = 0;
		for (Genotype genotype : genotypes) {
			GenotypeBuilder gtBuilder = new GenotypeBuilder().name(names.get(i));

			switch (genotype) {
			case HETEROZYGOUS:
				gtBuilder = gtBuilder.alleles(ImmutableList.of(refAllele, altAllele));
				break;
			case HOMOZYGOUS_REF:
				gtBuilder = gtBuilder.alleles(ImmutableList.of(refAllele, refAllele));
				break;
			case HOMOZYGOUS_ALT:
				gtBuilder = gtBuilder.alleles(ImmutableList.of(altAllele, altAllele));
				break;
			case NOT_OBSERVED:
				gtBuilder = gtBuilder.alleles(ImmutableList.of(uknAllele, uknAllele));
				break;
			}
			vcGenotypes.add(i, gtBuilder.make());
			i++;
		}
		return vcBuilder.genotypes(vcGenotypes).make();
	}

	protected VariantContextCompatibilityCheckerAutosomalDominant buildCheckerAD(Genotype gt)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(ImmutableList.of(gt));
		return new VariantContextCompatibilityCheckerAutosomalDominant(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalDominant buildCheckerAD(Genotype gt1, Genotype gt2)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(ImmutableList.of(gt1), ImmutableList.of(gt2));
		return new VariantContextCompatibilityCheckerAutosomalDominant(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalDominant buildCheckerAD(ImmutableList<Genotype> list)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(list);
		return new VariantContextCompatibilityCheckerAutosomalDominant(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalDominant buildCheckerAD(ImmutableList<Genotype> list1,
			ImmutableList<Genotype> list2) throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(list1, list2);
		return new VariantContextCompatibilityCheckerAutosomalDominant(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalRecessive buildCheckerAR(Genotype gt)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(ImmutableList.of(gt));
		return new VariantContextCompatibilityCheckerAutosomalRecessive(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalRecessive buildCheckerAR(Genotype gt1, Genotype gt2)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(ImmutableList.of(gt1), ImmutableList.of(gt2));
		return new VariantContextCompatibilityCheckerAutosomalRecessive(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalRecessive buildCheckerAR(ImmutableList<Genotype> list)
			throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(list);
		return new VariantContextCompatibilityCheckerAutosomalRecessive(pedigree, lst);
	}

	protected VariantContextCompatibilityCheckerAutosomalRecessive buildCheckerAR(ImmutableList<Genotype> list1,
			ImmutableList<Genotype> list2) throws CompatibilityCheckerException {
		List<VariantContext> lst = getInheritanceVariantContextList(list1, list2);
		return new VariantContextCompatibilityCheckerAutosomalRecessive(pedigree, lst);
	}

	protected CompatibilityCheckerXRecessive buildCheckerXR(Genotype gt) throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(ImmutableList.of(gt)));
		return new CompatibilityCheckerXRecessive(pedigree, lst);
	}

	protected CompatibilityCheckerXRecessive buildCheckerXR(Genotype gt1, Genotype gt2)
			throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true,
				ImmutableList.of(ImmutableList.of(gt1), ImmutableList.of(gt2)));
		return new CompatibilityCheckerXRecessive(pedigree, lst);
	}

	protected CompatibilityCheckerXRecessive buildCheckerXR(ImmutableList<Genotype> list)
			throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(list));
		return new CompatibilityCheckerXRecessive(pedigree, lst);
	}

	protected CompatibilityCheckerXRecessive buildCheckerXR(ImmutableList<Genotype> list1,
			ImmutableList<Genotype> list2) throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(list1, list2));
		return new CompatibilityCheckerXRecessive(pedigree, lst);
	}

	protected CompatibilityCheckerXDominant buildCheckerXD(Genotype gt) throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(ImmutableList.of(gt)));
		return new CompatibilityCheckerXDominant(pedigree, lst);
	}

	protected CompatibilityCheckerXDominant buildCheckerXD(ImmutableList<Genotype> list)
			throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(list));
		return new CompatibilityCheckerXDominant(pedigree, lst);
	}

	protected CompatibilityCheckerXDominant buildCheckerXD(ImmutableList<Genotype> list1, ImmutableList<Genotype> list2)
			throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true, ImmutableList.of(list1, list2));
		return new CompatibilityCheckerXDominant(pedigree, lst);
	}

	protected CompatibilityCheckerXDominant buildCheckerXD(Genotype gt1, Genotype gt2)
			throws CompatibilityCheckerException {
		GenotypeList lst = new GenotypeList(geneName, names, true,
				ImmutableList.of(ImmutableList.of(gt1), ImmutableList.of(gt2)));
		return new CompatibilityCheckerXDominant(pedigree, lst);
	}

	protected ImmutableList<Genotype> lst(Genotype... gts) {
		ImmutableList.Builder<Genotype> builder = new ImmutableList.Builder<Genotype>();
		for (int i = 0; i < gts.length; ++i)
			builder.add(gts[i]);
		return builder.build();
	}

}
