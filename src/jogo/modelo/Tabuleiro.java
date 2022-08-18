package jogo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{

	private final int linhas; // tamanho horizontal do tabuleiro
	private final int colunas; // tamanho vertical do tabuleiro
	private final int minas; // quantidade de minas
	
	// Lista de campos que ser�o abertos pelo jogador
	private final List<Campo> campos = new ArrayList<>();
	
	// Ir� determinar se o jogador ganhoou ou perdeu a 
	// parti de um valor Boolean
	private final List<Consumer<ResultadoEvento>> observadores =
			 new ArrayList<>();
	
    // Contrutor p�blico pois ser� criado um tabuleiro 
	// fora do Package Modelo	
	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		// s�o execultados na primeira vez que o codigo inicializa
		gerarCampo();
		associarizinhos();
		sortearMinas();
	}
	
	/*
	 * metodo que permite pecorre todos os elementos recebendo
	 * um tipo Campo que far� alguma a��o a parti das jogadas
	 * efetuadas pelo jogador
	 */ 
	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	// metodo que registra a Interface Consume 'observadores'
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		this.observadores.add(observador);
	}

	// notifica os observadores
	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
	/*
	 * Esse metodo abre o campo quando o jogador faz sua jogada
	 * abrindo um campo e por consequ�ncia, uma �rea do tabuleiro
	 */
	public void abrir(int linha, int coluna) {
			campos.parallelStream()
				.filter(c -> c.getLinhaX() == linha && c.getColunaY() == coluna)
				.findFirst()
				.ifPresent(c -> c.abir());;
	}
	
	// esse metodo permite o jogador marcar o campo no qual
	// ele acha que est� minado.
	public void marcar(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinhaX() == linha && c.getColunaY() == coluna)
			.findFirst()
			.ifPresent(c -> c.alternarMarcacao());;
				
	}

	// metodo que gera os campos basedo nas linhas e colunas
	private void gerarCampo() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}
	
	/*
	 * O metodo ir� percorre duas vezes a lista e far� com que
	 * todos os campos sejam vizinhos e com isso determinar 
	 * a vizinhan�a no tabuleiro de acordo com a proximidade
	 */
	private void associarizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	// metodo que ir� por as minas no tabuleiro qaundo o jogo for inicializado
	private void sortearMinas() {
		
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		// defini o la�o que ir� ficar execultando at� a quantidade
		// de minas armadas for menor que a quantiade de minas pr�-determinadas
		do {
	
			/*
			 * calculando um valor aleat�rio para determinado o
			 * valor de minas de modo que seja completado o la�o
			 * 'do while' e o tabuleiro seja preenchido com as minas
			 * de modo aleat�rio
			 */
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			
			// Sabe-se a quantidade de minas armadas pela quantidade de
			// campos que tem o atributo 'minado' = true.
			minasArmadas = campos.stream().filter(minado).count();
			
		} while(minasArmadas < minas);
	}
	
	// Diz a vit�ria do jogador
	public boolean objetivoAlcancado() {
		/*
		 * Se todos os objetivos dos campos no metodo objetivoAlcancado()
		 * na classe Campo forem alcancados, 
		 * este metodo ir� confirma a vit�ria do jogador conferindo cada 
		 * objetivo alcan�ado
		 */
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	// reinicia o jogo, ou seja, os campos
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	} 
	 
	 /*
	  * determina os eventos caso o jogador fa�a o acionamento de 
	  * uma mina que ir� explodi, assim perdendo o jogo e 
	  * o que acontece se caso ele cumpra os objetivos do jogo, 
	  * desse modo ganhando
	  */
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if(objetivoAlcancado()) {
			notificarObservadores(true);
		}
		
	}
	
	 /*
	 * Se caso o jogador perca a partida acionando uma mina, todas
	 * as minas do campo ser�o revelados por esse metodo atrav�s da 
	 * logica implementada, com exce��o dos campos j� marcados 
	 * pelo jogador em jogadas anteriores
	 */
	private void mostrarMinas () {
		campos.stream().filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
}
