package jogo.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import jogo.modelo.Campo;
import jogo.modelo.CampoEvento;
import jogo.modelo.CampoObservador;

/*
 * Nesta classe são criados e definidos todas as acões 
 * dos botões do tabuleiro, os quais servem
 * como campos de minas no jogo
 */
@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener{

	// Cores dos campos segundo cada efeito
	private final Color BG_PADRAO = new Color(200, 200, 200);
	private final Color BG_MARCADO = new Color(130, 150, 200);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE	 = new Color(0, 100, 0);
	private final Color BG_ABERTO = new Color(85, 95, 95);
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		
		this.campo = campo;
		setBackground(BG_PADRAO);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		campo.registrarObservador(this);
	}
	
	// A parti do momento que forem recebidos determinados eventos,
	// será aplicado diferentes tipos estilos para o botão/campo do jogo
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		
		switch(evento) {
		
		case ABRIR: 
			aplicarEstiloAbrir();
			break;
		
		case MARCAR:
			aplicarEstiloMarcar();
			break;

		case EXPLODIR:
			aplicarEstiloExplodir();
			break;	
			
		default: 
			aplicarEstiloPadrao();
		}
		
		// garante que os campos estarão 100% atualizados após um jogo
		SwingUtilities.invokeLater(()->{
			repaint();
			validate();
		});
	}
	
	// metodo que irá deixar o campo fechado como estilo padrao na interface
	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	// metodo que irá explodir na interface os campos
	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}

	// metodo que irá marcar na interface os campos
	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCADO);
		setText("T");
	}

	// metodo que irá abrir na interface os campos
	private void aplicarEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(BG_ABERTO));
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			setForeground(Color.WHITE);
			setText("X");
			return;
		}
		
		setBackground(BG_PADRAO);
		
		// sinaliza a quantidade de minas na vizinhaça pelas 
		// seguintes cores nos numeros
		switch (campo.minasNaVizinhanca()) {
		case 1 :
			setForeground(TEXTO_VERDE);
			break;
		case 2 :
			setForeground(Color.BLUE);
			break;	
		case 3 :
			setForeground(Color.YELLOW);
			break;	
		case 4 :
		case 5 :
		case 6 :
			setForeground(Color.RED);
			break;		
		default:
			setForeground(Color.PINK);
			break;
		}
		
		// mostra a quantidade de bombas proximas ao campo que 
		// o jogador abriu
		String valor = !campo.vizinhacaSegura() ? 
				campo.minasNaVizinhanca() + "" : "";
		setText(valor);
	}
	
	// Eventos do mouse na interface
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abir();
		} else {
			campo.alternarMarcacao();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
}
