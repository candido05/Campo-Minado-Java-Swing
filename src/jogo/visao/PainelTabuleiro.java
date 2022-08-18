package jogo.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jogo.modelo.Tabuleiro;

// essa classe é responsavel por exibir os botões 
// da interface gráfica do jogo
@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel{

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		// gera todos os campos do tabuleiro
		tabuleiro.paraCada(c -> add(new BotaoCampo(c)));
		
		// informa que o jogador venceu ou perdeu e reinicia o jogo
		tabuleiro.registrarObservador(e -> {
			
			SwingUtilities.invokeLater(() -> {
				if(e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "GANHOU!!! :)");
				} else {
					JOptionPane.showMessageDialog(this, "PERDEU :(");
					}
				
				// chama a reinicialização o jogo
				tabuleiro.reiniciar();
			});
		});
	}
}
