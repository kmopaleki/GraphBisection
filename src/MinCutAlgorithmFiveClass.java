import java.util.Random;

/**
 * User: Kevin
 * Date: 11/10/12
 */
public class MinCutAlgorithmFiveClass {
    private Integer numberOfRuns;
    private Long randomGenSeed;
    private Random randomValue;
    private Integer numberOfEvals;
    private String solutionFile;
    private String logFile;
    private int populationSize;
    private int numChildren;
    private int numParents;
    private int kParent;
    private int kSurvival;
    private int nCrossNum;
    private int termNum;
    private int numVertices;
    private int bitFlipProb;
    private String selectionAlg;
    private String ReproductionAlg;
    private String survivorAlg;
    private String fitnessFunction;
    private String survivorStrat;
    private int penaltyScalar;

    public MinCutAlgorithmFiveClass() {
    }

    public MinCutAlgorithmFiveClass(Integer numberOfRuns, Long randomGenSeed, Random randomValue,
                                    Integer numberOfEvals, String solutionFile, String logFile,
                                    int populationSize, int numChildren, int numParents,
                                    int kParent, int kSurvival, int nCrossNum,
                                    int termNum, int numVertices, int bitFlipProb,
                                    String selectionAlg, String reproductionAlg, String survivorAlg,
                                    String fitnessFunction, String survivorStrat, int penaltyScalar) {
        this.numberOfRuns = numberOfRuns;
        this.randomGenSeed = randomGenSeed;
        this.randomValue = randomValue;
        this.numberOfEvals = numberOfEvals;
        this.solutionFile = solutionFile;
        this.logFile = logFile;
        this.populationSize = populationSize;
        this.numChildren = numChildren;
        this.numParents = numParents;
        this.kParent = kParent;
        this.kSurvival = kSurvival;
        this.nCrossNum = nCrossNum;
        this.termNum = termNum;
        this.numVertices = numVertices;
        this.bitFlipProb = bitFlipProb;
        this.selectionAlg = selectionAlg;
        ReproductionAlg = reproductionAlg;
        this.survivorAlg = survivorAlg;
        this.fitnessFunction = fitnessFunction;
        this.survivorStrat = survivorStrat;
        this.penaltyScalar = penaltyScalar;
    }


}
