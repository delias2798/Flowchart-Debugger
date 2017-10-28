

public class Nodo_Doble<D>
{
	private D dato;
	private Nodo_Doble<D> anterior, siguiente;

	
	public Nodo_Doble(){}
	public Nodo_Doble(D dato)
	{
		this.dato = dato;
	}

	
	public D getDato(){return dato;}
	public void setDato(D dato){this.dato = dato;}
	
	public Nodo_Doble<D> getAnterior(){return anterior;}
	public void setAnterior(Nodo_Doble<D> anterior){this.anterior = anterior;}
	
	public Nodo_Doble<D> getSiguiente(){return siguiente;}
	public void setSiguiente(Nodo_Doble<D> siguiente){this.siguiente = siguiente;}
}
