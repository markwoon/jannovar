package de.charite.compbio.jannovar.pedigree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.charite.compbio.jannovar.pedigree.compatibilitychecker.CompatibilityCheckerException;

public class VCCompatibilityCheckerAutosomalDominantVerySmallTest extends AbstractCompatibilityCheckerTest {

	@Before
	public void setUp() throws PedParseException {
		ImmutableList.Builder<PedPerson> individuals = new ImmutableList.Builder<PedPerson>();
		individuals.add(new PedPerson("ped", "I.1", "0", "0", Sex.MALE, Disease.UNAFFECTED)); // father
		individuals.add(new PedPerson("ped", "II.1", "I.1", "0", Sex.MALE, Disease.AFFECTED)); // son
		PedFileContents pedFileContents = new PedFileContents(new ImmutableList.Builder<String>().build(),
				individuals.build());
		this.pedigree = new Pedigree(pedFileContents, "ped");

		this.names = ImmutableList.of("I.1", "II.1");
	}

	@Test
	public void testCaseNegativesOneVariant() throws CompatibilityCheckerException {
		Assert.assertFalse(!buildCheckerAD(lst(HET, HET)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(REF, REF)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(ALT, ALT)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(UKN, UKN)).run().isEmpty());
		
		Assert.assertFalse(!buildCheckerAD(lst(REF, ALT)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(HET, ALT)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(UKN, ALT)).run().isEmpty());
		
		
		Assert.assertFalse(!buildCheckerAD(lst(ALT, HET)).run().isEmpty());
		
		// at least one het
		Assert.assertFalse(!buildCheckerAD(lst(REF, UKN)).run().isEmpty());

	}

	@Test
	public void testCaseNegativesTwoVariants() throws CompatibilityCheckerException {
		Assert.assertFalse(!buildCheckerAD(lst(HET, HET), lst(HET, HET)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(ALT, HET), lst(HET, HET)).run().isEmpty());
		Assert.assertFalse(!buildCheckerAD(lst(ALT, HET), lst(REF, UKN)).run().isEmpty());


	}

	@Test
	public void testCasePositiveOneVariant() throws CompatibilityCheckerException {
		Assert.assertTrue(buildCheckerAD(lst(REF, HET)).run().size() == 1);
		Assert.assertTrue(buildCheckerAD(lst(UKN, HET)).run().size() == 1);
	}

	@Test
	public void testCasePositiveTwoVariants() throws CompatibilityCheckerException {
		Assert.assertTrue(buildCheckerAD(lst(ALT, HET), lst(REF, HET)).run().size() == 1);
		Assert.assertTrue(buildCheckerAD(lst(UKN, HET), lst(UKN, HET)).run().size() == 2);
		Assert.assertTrue(buildCheckerAD(lst(HET, HET), lst(REF, HET)).run().size() == 1);
	}

}
