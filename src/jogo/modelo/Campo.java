package jogo.modelo;

import java.util.ArrayList;
import java.util.List;


//Classe que define os campos do tabuleiro
public class Campo {

	// Define o campo est? com uma mina ou n?o 
	private boolean minado = false;
	
	/* 
	 * Define se o campo estar? aberto ou fechado
	 * a parti das jogadas do jogador
	 * 
	 * Inicia como sendo 'false', pois o campo s?
	 * ser? aberto com uma jogada, at? isso acontecer,
	 * o campo fica fechado.
	*/ 
	private boolean aberto = false;
	
	// Define a marca??o feita pelo jogado ao 
	// sinalizar se um campo est? ou n?o minado;
	private boolean marcado = false;
	
	// Duas variaveis que ir?o ser as coordenadas
	// do tabuleiro.
	private final int linhaX;
	private final int colunaY;
	
	// Define a ?rea que ? expandida ao redor do campo no 
	// qual o jogador clicou quando fez sua jogada.
	private List<Campo> vizinhos = new ArrayList<Campo>();
	
	// para registrar os observadores, ser? nescessario criar uma
	// classe publica com o nome de: registrarObservador
	private List<CampoObservador> observadores = new ArrayList<>();
	
	//Metodo construtor 
	Campo( int linhaX, int colunaY){
		this.linhaX = linhaX;
		this.colunaY = colunaY;
	}
	
	public void registrarObservador (CampoObservador observador) {
		observadores.add(observador);
	}
	
	// este metodo ser? chamando internamente na classe Campo 
	// para chamar os observadores quando um evento acontecer
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	/* Defini a logica para um campo ser vizinho ou n?o
	 * 
	 *  Por exemplo, define que o campo 0x0 seja vizinho do
	 *  campo 0x1 e impede que seja vizinho do campo 10x5;
	 */
	boolean adicionarVizinho(Campo vizinho) {
		
		boolean linhaDiferente = linhaX != vizinho.linhaX;
		boolean colunaDiferente = colunaY != vizinho.colunaY;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		// Linha ou Coluna, diferen?a numero = 1
		// Diagonal, diferen?a numerica = 2;
		int deltaLinha = Math.abs(linhaX - vizinho.linhaX); 
		int deltaColuna = Math.abs(colunaY - vizinho.colunaY); 
		int deltaGeral = deltaColuna + deltaLinha;
		
		/*
		 *  Verifica, define e adciona o campo como vizinho 
		 *  lateral (mesma linhaX) e/ou vertical (mesma colunaY).
		 *  
		 *	Tamb?m Verifica, define e adciona o campo como vizinho
		 *  diagonal (1 coluna e linha a menos ou a mais)
		 */	
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	// Marca?ao de um campo pelo jogador
	public void alternarMarcacao() {
		if(!aberto) {
			// alternancia da marca??o
			marcado = !marcado;
		}
		
		if(marcado) {
			notificarObservadores(CampoEvento.MARCAR);
		} else {
			notificarObservadores(CampoEvento.DESMARCAR);
		}
	}
	
	// impede que o jogador abra um campo enquanto
	// a marca??o que o mesmo jogador p?s estiver valendo
	public boolean abir() {
		if (!aberto && !marcado) {
			
			// Se o jogador entrar nessa IF o jogo est?
			// perdido pois acionou uma mina.
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			// Se o jogador entrar nessa exe??o uma area
			// segura ser? aberta
			if (vizinhacaSegura()) {
				vizinhos.forEach(v -> v.abir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 *  garante a ?rea de espans?o do campo quando o jogador
	 *  faz sua jogada garantido que durante a abertura da ?rea
	 *  n?o seja acionada uma mina, apenas se caso o jogador 
	 *  click em um campo com uma mina.
	 */
	public boolean vizinhacaSegura() {
		 /*
		  * Se o rsultado for false, significa que ao menos
		  * um dos vizinhos est? minado
		  * Se caso o reultado for true, significa que a 
		  * vizinhan?a est? seguro, pois nenhum campo pode
		  * dar match no outro
		  */
		 return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	//metodo que mina um campo
	void minar() {
		minado = true;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		/*
		 * Se for aberto, entao = true. Caso isso aconte?a, os 
		 * observadores s?o notificados que aconteceu um evento 
		 * de ABIR um campo, que s? ira acontecer caso o campo aberto
		 * for verdadeiro 
		 */
		if (aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinhaX() {
		return linhaX;
	}

	public int getColunaY() {
		return colunaY;
	}
	
	// nesse metodo ? determinado a vitoria do jogado
	// caso ele consiga marca todos os campos com minas
	boolean objetivoAlcancado() {
		// Atigindo um dos objetivos, ? conquitado um dos
		// objetivos do jogo para UM DOS CAMPOS
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		
		return desvendado || protegido;
	}
	
	// metodo que mostrara os numeros nas vizinhancas do campos
	// qunado o jogador abre
	public int minasNaVizinhanca() {
		// Filter ? usado para filtrar apenas os vizinhos
		// que t?m minas.
		return (int)vizinhos.stream().filter(v -> v.minado).count();
	}
	
	/*
	 *  reinicia o jogo
	 *  --> A logica para a coloca??o aleatorias das minas e 
	 *  da restaura??o do campo est? na classe Tabuleiro <--
	 */
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
}
