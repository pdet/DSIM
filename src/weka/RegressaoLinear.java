/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weka;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author pedroholanda
 */
public class RegressaoLinear {

    private ArrayList<Integer> datas365 = new ArrayList<Integer>();
    private ArrayList<Integer> datas = new ArrayList<Integer>();
    private String aux;
    private Object valor;
    private int xTot = 0;
    private int yTot = 0;
    private int x2 = 0;
    private int xy = 0;
    private int numeroInstancias;
    private int numeroInstanciasTeste;
    private DecimalFormat deci = new DecimalFormat("0.00");

    public void instanciastoArrayList(String instancias) throws Exception {
        char c;
        boolean primeiraLeitura = true;
        String numero = "";
        int day;
        for (int i = 0; i < instancias.length(); i++) {
            c = instancias.charAt(i);
            if (c == ',' && primeiraLeitura == true) {
                c = instancias.charAt(i - 1);
                while (Character.isDigit(c)) {
                    i--;
                    c = instancias.charAt(i);
                }
                primeiraLeitura = false;


            } else if (primeiraLeitura == false) {
                if (Character.isDigit(c)) {
                    numero = numero + c;
                } else {
                    day = Integer.parseInt(numero);
                    datas365.add(day);
                    numero = "";
                }
            }
        }
        day = Integer.parseInt(numero);
        datas365.add(day);


    }

    private void dadosCrescentes() {
        boolean primeiroAno = true;
        int day;
        datas.add(datas365.get(0));
        for (int i = 3; i < datas365.size(); i = i + 3) {
            if (primeiroAno == true) {
                if (datas365.get(i) < datas365.get(i - 3)) {
                    day = datas365.get(i) + datas365.get(i - 3);
                    datas.add(day);
                    primeiroAno = false;
                } else {
                    datas.add(datas365.get(i));
                }
            } else {
                if (datas365.get(i) < datas365.get(i - 3)) {
                    day = datas365.get(i) + datas.get((i / 3) - 1);
                    datas.add(day);
                } else {
                    day = (datas365.get(i) - datas365.get(i - 3)) + datas.get((i / 3) - 1);
                    datas.add(day);
                }
            }


        }
        for (int i = 2; i > 0; i--) {
            if (datas365.get(datas365.size() - i) < datas365.get(datas365.size() - i - 1)) {
                day = datas365.get(datas365.size() - i) + datas.get((i / 3) - 1);
                datas.add(day);
            } else {
                day = (datas365.get(datas365.size() - i) - datas365.get(datas365.size() - i - 1)) + datas.get(datas.size() - 1);
                datas.add(day);
            }
        }
        String iradissima="";
        for (int i = 0; i<datas.size();i++){
            //iradissima = iradissima + String.valueOf(datas.get(i)) +"," +String.valueOf(datas.get(i+1)) + ","+ String.valueOf(datas.get(i+2)) + "\n";
            iradissima =iradissima + ","+ String.valueOf(datas.get(i));
        }
        System.out.println(iradissima);

    }

    private double erroQuadraticoMedio(ArrayList<Double> erro){
        double erroQuadraticoMedio= 0;
        for (int i = 0; i < erro.size();i++){
            erroQuadraticoMedio = erroQuadraticoMedio + erro.get(i)*erro.get(i);           
        }
        erroQuadraticoMedio = erroQuadraticoMedio/erro.size();
        return erroQuadraticoMedio;
    }
    private void getXYTotal(int numeroInstanciasTeste) {
        int x;
        int y;
        for (int i = 0; i < numeroInstanciasTeste-1; i++) {
            x = datas.get(i);
            xTot = x + xTot;
            x2 = x2 + x * x;
            y = datas.get(i + 1);
            yTot = y + yTot;
            xy = xy + x * y;

        }
        numeroInstancias = datas.size();


    }

    private double getCoeficienteAngular() {
        double coefAngular;
        coefAngular = ((xy) - ((xTot / numeroInstanciasTeste) * (yTot / numeroInstanciasTeste) * numeroInstanciasTeste));
        coefAngular = coefAngular / ((x2) - ((xTot) / numeroInstanciasTeste * (xTot) / numeroInstanciasTeste) * numeroInstanciasTeste);
        return coefAngular;
    }

    private long getCoeficienteLinear(double coefAngular) {
        long coefLinear;
        coefLinear = (long) ((yTot / numeroInstanciasTeste) - (coefAngular * (xTot / numeroInstanciasTeste)));
        return coefLinear;
    }

    public String getPrevisoes(int porcentagem) {
        ArrayList<Double> erro = new ArrayList<Double>();
        this.dadosCrescentes();
        numeroInstanciasTeste = datas.size();
        if (porcentagem != 100) {
            numeroInstanciasTeste = (datas.size() * porcentagem) / 100;
        }
        this.getXYTotal(numeroInstanciasTeste);
        double coefAngular = this.getCoeficienteAngular();
        double coefLinear = this.getCoeficienteLinear(coefAngular);
        String previsoes = "Atual           Previsto" + "\n";
        double dataPrevista;
        int data;
        if (numeroInstanciasTeste == datas.size()) {
            numeroInstanciasTeste = 1;
        }
        for (int i = numeroInstanciasTeste-1; i < numeroInstancias-1; i++) {
            data = datas.get(i);
            dataPrevista = (coefLinear + (coefAngular * data));
            data = datas.get(i + 1);
            previsoes = previsoes + String.valueOf(data) + "           " + String.valueOf(deci.format(dataPrevista)) + "\n";
            erro.add(dataPrevista - data);
        }
        previsoes =previsoes + "Erro Quadratico Médio :" + String.valueOf(this.erroQuadraticoMedio(erro));
        return previsoes;      
    }
}
