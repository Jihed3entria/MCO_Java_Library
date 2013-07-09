package mco_java_library_tests;
import methods.AHP.AHP;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleEVD;
import methods.Criterium;
import methods.Alternative;

/**
 *
 * @author Mateusz Krasucki
 */
public class AHP_test {

    /**
     * @param args the command line arguments
     */
    public static void test()   {

	double[][] style = {
			{1.0000, 0.2500, 4.0000, 0.1667},
			{4.0000, 1.0000, 4.0000, 0.2500},
			{0.2500, 0.2500, 1.0000, 0.2000},
                        {6.0000, 4.0000, 5.0000, 1.0000}
	};
        
        double[][] reliability = {
			{1.0000, 2.0000, 5.0000, 1.0000},
			{0.5000, 1.0000, 3.0000, 2.0000},
			{0.2000, 0.3333, 1.0000, 0.2500},
                        {1.0000, 0.5000, 4.0000, 1.0000}
	};
        
        double[][] fuelEconomy = {
			{1.0000, 1.2593, 1.4167, 1.2143},
			{0.0000, 1.0000, 1.1250, 0.9643},
			{0.0000, 0.0000, 1.0000, 0.8571},
                        {0.0000, 0.0000, 0.0000, 1.0000}
	};
                
        double[][] criteria = {
			{1.0000, 0.5000, 3.0000},
			{0.0000, 1.0000, 4.0000},
			{0.0000, 0.0000, 1.0000}
	};
        
        AHP ahpTest = new AHP();
        
        Criterium c_style = new Criterium();
        c_style.name = "style";
        c_style.direction = Criterium.Direction.MAX;
        ahpTest.addCriterium(c_style);
        
        Criterium c_reliability = new Criterium();
        c_reliability.name = "reliability";
        c_reliability.direction = Criterium.Direction.MAX;
        ahpTest.addCriterium(c_reliability);
        
        Criterium c_fuelEconomy = new Criterium();
        c_fuelEconomy.name = "fuelEconomy";
        c_fuelEconomy.direction = Criterium.Direction.MAX;
        ahpTest.addCriterium(c_fuelEconomy);
        
       
        ahpTest.setCriteriaMatrix(criteria, true);
        
        Alternative alt1 = new Alternative();
        alt1.name = "car1";
        ahpTest.addAlternative(alt1);
        Alternative alt2= new Alternative();
        alt2.name = "car2";
        ahpTest.addAlternative(alt2);
        Alternative alt3 = new Alternative();
        alt3.name = "car3";
        ahpTest.addAlternative(alt3);
        Alternative alt4 = new Alternative();
        alt4.name = "car4";
        ahpTest.addAlternative(alt4);
        
        ahpTest.addAltsCriteriumValues(style, false);
        ahpTest.addAltsCriteriumValues(reliability, false); 
        ahpTest.addAltsCriteriumValues(fuelEconomy, true);
        
        ahpTest.calculate();
        
        System.out.println(ahpTest.getCriteriumWeight(0));
        System.out.println(ahpTest.getCriteriumWeight(1));
        System.out.println(ahpTest.getCriteriumWeight(2));
        
        System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(0, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(1, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(2, 2));
        
                System.out.println();
        
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 0));
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 1));
        System.out.println(ahpTest.getAlternativeCriteriumValue(3, 2));
        
        
        System.out.println();
        
        System.out.println(ahpTest.alternatives.get(0).name + " " + ahpTest.getAlternativeValue(0));
        System.out.println(ahpTest.alternatives.get(1).name + " " + ahpTest.getAlternativeValue(1));
        System.out.println(ahpTest.alternatives.get(2).name + " " + ahpTest.getAlternativeValue(2));
        System.out.println(ahpTest.alternatives.get(3).name + " " + ahpTest.getAlternativeValue(3));
    }
}
