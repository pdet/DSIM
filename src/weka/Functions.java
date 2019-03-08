package weka;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Functions {

    Weka teste = new Weka();
    String allData;
    RegressaoLinear rl = new RegressaoLinear();

    public String mlp(String diretorioArquivo, String learningRate, String trainingTime, String layers, String folds) throws FileNotFoundException, IOException, Exception {
        teste.lerArquivo(diretorioArquivo);
        teste.multilayerPerceptron(Double.parseDouble(learningRate), Integer.parseInt(trainingTime), layers);
        allData = teste.crossValidation(Integer.parseInt(folds), "mlp");
        return allData;
    }

    public String rbf(String diretorioArquivo, String clusters, String folds) throws FileNotFoundException, IOException, Exception {
        teste.lerArquivo(diretorioArquivo);
        teste.radialbasisFunction(Integer.parseInt(clusters));
        allData = teste.crossValidation(Integer.parseInt(folds), "rbf");
        return allData;
        
    }
    
    public String regressaoLinear(String diretorio,int porcentagem) throws FileNotFoundException, Exception {
        rl.instanciastoArrayList(teste.arquivoString(diretorio));
        allData = rl.getPrevisoes(porcentagem);
        return allData;
        
    }}
