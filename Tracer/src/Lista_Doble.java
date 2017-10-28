

public class Lista_Doble<D>
{	
	private int cantidad_de_nodos;
	private Nodo_Doble<D> primero, ultimo;
	
	public Lista_Doble(){}
	public Lista_Doble(D dato)
	{
		cantidad_de_nodos = 0;
		insertar_en_vacia(dato);
	}
	
	public int getCantidad_de_nodos() {
		return cantidad_de_nodos;
	}
	public void setCantidad_de_nodos(int cantidad_de_nodos) {
		this.cantidad_de_nodos = cantidad_de_nodos;
	}
	public Nodo_Doble<D> getPrimero() {
		return primero;
	}
	public void setPrimero(Nodo_Doble<D> primero) {
		this.primero = primero;
	}
	public Nodo_Doble<D> getUltimo() {
		return ultimo;
	}
	public void setUltimo(Nodo_Doble<D> ultimo) {
		this.ultimo = ultimo;
	}
	
	public boolean esta_vacia()
	{
		 return primero == null;
	}
	
	private void insertar_en_vacia(D dato)
	{
		primero = ultimo = new Nodo_Doble<D>(dato);
		cantidad_de_nodos++;
	}
	
	public void insertar_al_inicio(D dato)
	{
		if(esta_vacia())
		{
			insertar_en_vacia(dato);
		}
		else
		{
			Nodo_Doble<D> nodo_nuevo = new Nodo_Doble<>(dato);
			nodo_nuevo.setSiguiente(primero);
			primero.setAnterior(nodo_nuevo);
			primero = nodo_nuevo;
		}
		cantidad_de_nodos++;
	}
	
	public void insertar_al_final(D dato)
	{
		if(esta_vacia())
		{
			insertar_en_vacia(dato);
		}
		else
		{
			Nodo_Doble<D> nodo_nuevo = new Nodo_Doble<>(dato);
			ultimo.setSiguiente(nodo_nuevo);
			nodo_nuevo.setAnterior(ultimo);
			ultimo = nodo_nuevo;
			cantidad_de_nodos++;
		}
	}	
	
	public boolean insertar_al_medio(D dato, int posicion)
	{
		if(esta_vacia())
		{
			insertar_en_vacia(dato);
		}
		else if(posicion < 1 || posicion >= cantidad_de_nodos)
		{
			return false;
		}
		else
		{
			Nodo_Doble<D> pivote = primero;
			for(int i=0; i < posicion-1; i++)
			{
				pivote = pivote.getSiguiente();
			}
			Nodo_Doble<D> nodo_nuevo = new Nodo_Doble<>(dato);
			nodo_nuevo.setSiguiente(pivote.getSiguiente());
			pivote.getSiguiente().setAnterior(nodo_nuevo);
			nodo_nuevo.setAnterior(pivote);
			pivote.setSiguiente(nodo_nuevo);
			cantidad_de_nodos++;
		}
		return true;
	}	
	
	public boolean eliminar_al_inicio()
	{
		if(cantidad_de_nodos > 0)
		{
			if(cantidad_de_nodos == 1)
			{
				primero = ultimo = null;
			}
			else
			{
				primero = primero.getSiguiente();
				primero.setAnterior(null);
			}
			cantidad_de_nodos--;
			return true;
		}
		return false;
	}
	
	public boolean eliminar_al_final()
	{
		if(cantidad_de_nodos > 0)
		{
			if(cantidad_de_nodos == 1)
			{
				primero = ultimo = null;
			}
			else
			{
				ultimo = ultimo.getAnterior();
				ultimo.setSiguiente(null);				
			}
			cantidad_de_nodos--;
			return true;
		}
		return false;
	}
	
	public boolean eliminar_al_medio(int posicion)
	{
		if(cantidad_de_nodos > 0)
		{
			if(posicion >= cantidad_de_nodos)
			{
				return eliminar_al_final();
			}
			else if(posicion <= 0)
			{
				return eliminar_al_inicio();
			}
			else
			{
				Nodo_Doble<D> pivote = primero;
				for(int i=0; i < posicion-1; i++)
				{
					pivote = pivote.getSiguiente();
				}								
				pivote.getSiguiente().getSiguiente().setAnterior(pivote);
				pivote.setSiguiente(pivote.getSiguiente().getSiguiente());
				cantidad_de_nodos--;
				return true;
			}
		}
		return false;
	}	
//	private void eliminar_al_medio_sin_return(int posicion)
//	{
//		if(cantidad_de_nodos > 0)
//		{
//			if(posicion >= cantidad_de_nodos)
//			{
//				eliminar_al_final();
//			}
//			else if(posicion <= 0)
//			{
//				eliminar_al_inicio();
//			}
//			else
//			{
//				Nodo_Doble<D> pivote = primero;
//				for(int i=0; i < posicion-1; i++)
//				{
//					pivote = pivote.getSiguiente();
//				}
//				pivote.getSiguiente().getSiguiente().setAnterior(pivote);
//				pivote.setSiguiente(pivote.getSiguiente().getSiguiente());
//				cantidad_de_nodos--;
//			}
//		}
//	}
	public boolean eliminar(D dato)
	{
		if(!esta_vacia())
		{
			Nodo_Doble<D> pivote = primero;
			for(int i=0; i < cantidad_de_nodos; i++)
			{
				if(pivote.getDato().equals(dato))
				{
					if(i == 0)
					{
						eliminar_al_inicio();
					}
					else if(i == cantidad_de_nodos-1)
					{
						eliminar_al_final();
					}
					else
					{
						pivote.getAnterior().setSiguiente(pivote.getSiguiente());
						pivote.getSiguiente().setAnterior(pivote.getAnterior());
						cantidad_de_nodos--;
					}
					return true;
				}
				pivote = pivote.getSiguiente();
			}
		}
		return false;
	}

	public void borrar()
	{
		this.primero = null;
		this.ultimo = null;
		this.cantidad_de_nodos = 0;		
	}
	
	public D get_dato_por_indice(int posicion)
	{
		if(posicion > -1 && posicion < cantidad_de_nodos)
		{
			Nodo_Doble<D> pivote = primero;
			for(int i=0; i < posicion; i++)
			{
				pivote = pivote.getSiguiente();
			}			
			return pivote.getDato();
		}
		return null;
	}
	
	public D get_dato(D dato)
	{
		if(cantidad_de_nodos != 0)
		{
			Nodo_Doble<D> pivote = primero;
						
			while(pivote != null)
			{
				if(pivote.getDato() == dato)
				{
					return pivote.getDato();
				}
				else
				{
					pivote = pivote.getSiguiente();
				}
			}	
		}
		return null;
	}	

	public boolean existe(D dato)
	{
		return get_dato(dato) != null;
	}

	public void mostrar(boolean mostrar_estructura)
	{
		if(esta_vacia())
		{
			System.out.print("[<->]");
		}
		else
		{
			Nodo_Doble<D> pivote = this.primero;
			if(mostrar_estructura)
			{
				for(int i=0; i < cantidad_de_nodos; i++)
				{	
					System.out.print("<-[" + pivote.getDato().toString() + "]->");				
					pivote = pivote.getSiguiente();
				}
				System.out.println();
			}
			else
			{
				System.out.print("[");
				for(int i=0; i < cantidad_de_nodos; i++)
				{	
					if(i==0)
					{
						System.out.print(pivote.getDato().toString());
					}
					else
					{
						System.out.print("," + pivote.getDato().toString());
					}
					pivote = pivote.getSiguiente();
				}
				System.out.println("]");
			}
		}
	}

	
}