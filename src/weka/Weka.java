/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.Range;

/**
 *
 * @author Pedro Holanda
 */
public class Weka {

    private FileReader reader;
    private Instances instancias;
    private MultilayerPerceptron mlp = new MultilayerPerceptron();
    private RBFNetwork rbf = new RBFNetwork();
    String dados;
    
    public String arquivoString(String diretorio) throws FileNotFoundException, IOException, Exception {
        reader = new FileReader(diretorio);
        BufferedReader bReader = new BufferedReader(reader);
        instancias = new Instances(bReader);
        String aux = instancias.toString();
        return aux;


    }
    public void lerArquivo(String diretorio) throws FileNotFoundException, IOException, Exception {
        reader = new FileReader(diretorio);
        BufferedReader bReader = new BufferedReader(reader);
        instancias = new Instances(bReader);
        bReader.close();
        instancias.setClassIndex(instancias.numAttributes() - 1);

    }


    public void multilayerPerceptron(double learningRate, int trainingTime, String hiddenLayers) throws Exception {
        mlp.setAutoBuild(true);
        mlp.setLearningRate(learningRate);
        mlp.setMomentum(0.2);
        mlp.setTrainingTime(trainingTime);
        mlp.setHiddenLayers(hiddenLayers);
        mlp.buildClassifier(instancias);

    }

    public void radialbasisFunction(int numeroClusters) throws Exception {
       // rbf.setClusteringSeed(1);
       // rbf.setRidge(1.0);
       // rbf.setMaxIts(-1);
       // rbf.setMinStdDev(0.1);
       // rbf.setDebug(false);
        rbf.setDebug(true);
        rbf.setNumClusters(numeroClusters);
        rbf.buildClassifier(instancias);
    }

    public String crossValidation(int folds, String redeNeural) throws Exception {
        Evaluation eval = new Evaluation(instancias);
        StringBuffer predictions = new StringBuffer();
        String allData;
        if (redeNeural.equals("mlp")) {
            eval.crossValidateModel(mlp, instancias, folds, new Random(1), predictions, new Range("first,last"), false);
        } else if (redeNeural.equals("rbf")) {
            eval.crossValidateModel(rbf, instancias, folds, new Random(1), predictions, new Range("first,last"), false);
        }
        String[] lines = predictions.toString().split("\n");
        allData = eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false);
        for (int i = 0; i < eval.numInstances(); i++) {
            allData = allData + "\n" + lines[i].toString();

        }
        return allData;
    }
    

}
