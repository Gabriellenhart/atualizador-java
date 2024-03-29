/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.biopark.queijos.atualizador.util;

import br.biopark.queijos.atualizador.Progress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

/**
 *
 * @author Renato
 */
public class GitUtil {

    Progress janela = Progress.getInstance();
    Util util = new Util();

    public GitUtil() {
    }

    public List<String> buscaVersoes() {
        try {

            janela.getLbStatus().setText("Procurando por novas versões...");
            janela.repaint();
            util.sleep(5000);

            final Map<String, Ref> map = Git.lsRemoteRepository()
                    .setHeads(false)
                    .setTags(true)
                    .setRemote("https://github.com/renatofritola/Loteria.git")
                    .callAsMap();

            return readVersions(map);

//            Runtime rt = Runtime.getRuntime();
//            Process pr = rt.exec("git ls-remote --tags https://github.com/renatofritola/Loteria.git v*");
        } catch (GitAPIException ex) {
            Logger.getLogger(GitUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private List<String> readVersions(Map<String, Ref> map) {

        List<String> versoes = new ArrayList<>();

        janela.getLbStatus().setText("Verificando versão atual...");
        janela.repaint();
        util.sleep(5000);

        for (Map.Entry<String, Ref> entry : map.entrySet()) {
            if (entry.getKey().contains("refs/tags/v")) {

                String versao = entry.getKey().replace("refs/tags/v", "");
                versoes.add(versao);

            }

        }
        
        Collections.sort(versoes);
        
        return versoes;
    }

}
