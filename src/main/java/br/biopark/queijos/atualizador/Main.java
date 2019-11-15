/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.biopark.queijos.atualizador;

import java.util.logging.Level;
import java.util.logging.Logger;
import br.biopark.queijos.atualizador.util.PropFile;
import br.biopark.queijos.atualizador.util.Util;
import br.biopark.queijos.atualizador.enumerator.EPropertie;
import br.biopark.queijos.atualizador.util.GitUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

/**
 *
 * @author Renato
 */
public class Main {

    private static Util util = new Util();
    private static PropFile prop = new PropFile();
    private static String versaoAtual = "";
    private static List<String> versoes = new ArrayList();

    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        int delay = 5000;   // delay de 5 seg.
        int interval = 50000;//Integer.parseInt(prop.readPropertie(EPropertie.UPDATE_CHECK_FREQUENCY)) * 3600;  // intervalo configurado.
        Timer timer = new Timer();

        //timer.scheduleAtFixedRate(new TimerTask() {
        //    public void run() {

                versaoAtual = prop.readPropertie(EPropertie.APLICATION_VERSION);
                GitUtil git = new GitUtil();
                versoes = git.buscaVersoes();

                if (checkNewVersion()) {
                    
                    Object[] options = {"Atualizar", "Mais tarde..."};
                    int x =     JOptionPane.showOptionDialog(null, "Foi encontrada uma nova versão do sistema. Deseja atualizar agora? \n Obs.: O sistema será fechado para atualização.", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    
                    if (x == 0){
                        iniciarAtualizacao();
                    }
                }
            //}
       // }, delay, interval);

    }

    private static void iniciarAtualizacao() {
        Progress janela = Progress.getInstance();

        janela.setLocationRelativeTo(null);
        janela.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        janela.getJpProgress().setIndeterminate(true);
        janela.getLbVersao().setText(versaoAtual);
        janela.getJpProgress().setVisible(true);
        janela.getJpProgress().setStringPainted(true);
        
        janela.setVisible(true);
        janela.repaint();
        
        janela.processar(versoes);

    }

    private static boolean checkNewVersion() {

        int vAtual = Integer.parseInt(versaoAtual.replace(".", ""));

        for (String v : versoes) {

            int vRem = Integer.parseInt(v.replace(".", ""));

            if (vRem > vAtual) {
                return true;
            }

        }

        return false;
    }

}
