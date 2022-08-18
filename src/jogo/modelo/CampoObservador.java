package jogo.modelo;

public interface CampoObservador {

	/*
	 *  quando um evento ocorre, é necessario informar
	 *  em qual campo ocorreu e de qual o tipo de evento
	 */
	public void eventoOcorreu(Campo c, CampoEvento e);
}
