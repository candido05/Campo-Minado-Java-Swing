package jogo.modelo;

public class ResultadoEvento {

	/*
	 * esta calsse faz com que passe um objeto com informação
	 * true ou false ( ganhou ou perdeu) toda vez que é 
	 * instanciado na classe Tabuleiro
	 */
	private final boolean ganhou;

	public ResultadoEvento(boolean ganhou) {
		this.ganhou = ganhou;
	}

	public boolean isGanhou() {
		return ganhou;
	}
}
