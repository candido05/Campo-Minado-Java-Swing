package jogo.modelo;

public class ResultadoEvento {

	/*
	 * esta calsse faz com que passe um objeto com informa��o
	 * true ou false ( ganhou ou perdeu) toda vez que � 
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
