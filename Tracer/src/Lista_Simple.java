

public class Lista_Simple<T>
{	
	private int cantidad_de_nodos;
	private Nodo<T> primero, ultimo;

	public Lista_Simple(){}

	
	public int getCantidad_de_nodos(){return cantidad_de_nodos;}
	public void setCantidad_de_nodos(int cantidad_de_nodos){this.cantidad_de_nodos = cantidad_de_nodos;}
	
	public Nodo<T> getPrimero(){return primero;}
	public void setPrimero(Nodo<T> primero){this.primero = primero;}
	
	public Nodo<T> getUltimo(){return ultimo;}
	public void setUltimo(Nodo<T> ultimo){this.ultimo = ultimo;}

	public boolean esta_vacia()
	{return primero == null;}
	
	private void insertar_en_vacia(T dato)
	{
		primero = ultimo = new Nodo<T>(dato);
		cantidad_de_nodos++;
	}
	
	public void insertar_al_inicio(T dato)
	{
		if(esta_vacia())
		{
			insertar_en_vacia(dato);
		}
		else
		{
			Nodo<T> nodo_nuevo = new Nodo<>(dato);
			nodo_nuevo.setSiguiente(primero);
			primero = nodo_nuevo;
			cantidad_de_nodos++;
		}
	}	
	public void insertar_al_final(T o)
	{
		//System.out.println("insertar_al_final de: " + o);
		if(esta_vacia())
		{
			insertar_en_vacia(o);
		}
		else
		{
			//System.out.println("Lo q tiene el ultimo nodo ANTES de insertar: " + ultimo.getDato());
			Nodo<T> nodo_nuevo = new Nodo<>(o);
			ultimo.setSiguiente(nodo_nuevo);
			ultimo = nodo_nuevo;
			cantidad_de_nodos++;
			//System.out.println("Lo q tiene el ultimo nodo DESPUES de insertar: " + ultimo.getDato());
		}
		//System.out.println("Ahora hay: " + cantidad_de_nodos);
	}	
	public boolean insertar_al_medio(T dato, int posicion)
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
			Nodo<T> pivote = primero;
			for(int i=0; i < posicion-1; i++)
			{
				pivote = pivote.getSiguiente();
			}
			Nodo<T> nodo_nuevo = new Nodo<>(dato);
			nodo_nuevo.setSiguiente(pivote.getSiguiente());
			pivote.setSiguiente(nodo_nuevo);
			cantidad_de_nodos++;
		}
		return true;
	}
	
	public boolean sobreescribir(int posicion, T dato)
	{
		if(!esta_vacia())
		{
			if(posicion >= 0 && posicion < cantidad_de_nodos)
			{
				Nodo<T> pivote = primero;
				for(int i=0; i < posicion; i++)
				{
					pivote = pivote.getSiguiente();
				}
				pivote.setDato(dato);
				return true;
			}
		}
		return false;
	}
	public void sobreescribir(Lista_Simple<Integer> posiciones, Lista_Simple<T> datos)
	{
		if(!esta_vacia())
		{
			for(int i=0; i < posiciones.getCantidad_de_nodos(); i++)
			{
				if(posiciones.get_dato_por_indice(i) >= 0 && posiciones.get_dato_por_indice(i) < cantidad_de_nodos)
				{
					Nodo<T> pivote = primero;
					for(int j=0; j < posiciones.get_dato_por_indice(i); j++)
					{
						pivote = pivote.getSiguiente();
					}
					pivote.setDato(datos.get_dato_por_indice(i));
				}				
			}
		}
	}
	public Lista_Simple<T> clonar()
	{
		Lista_Simple<T> clon = new Lista_Simple<>();
		Nodo<T> pivote = this.primero;
		for(int i=0; i < cantidad_de_nodos; i++)
		{
			clon.insertar_al_final(pivote.getDato());
			pivote = pivote.getSiguiente();
		}
		return clon;
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
				Nodo<T> pivote = primero;
				while(pivote.getSiguiente() != ultimo)
				{
					pivote = pivote.getSiguiente();
				}
				pivote.setSiguiente(null);
				ultimo = pivote;
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
				Nodo<T> pivote = primero;
				for(int i=0; i < posicion-1; i++)
				{
					pivote = pivote.getSiguiente();
				}				
				pivote.setSiguiente(pivote.getSiguiente().getSiguiente());
				cantidad_de_nodos--;
				return true;
			}
		}
		return false;
	}

	private void eliminar_al_medio_sin_return(int posicion)
	{
		if(cantidad_de_nodos > 0)
		{
			if(posicion >= cantidad_de_nodos)
			{
				eliminar_al_final();
			}
			else if(posicion <= 0)
			{
				eliminar_al_inicio();
			}
			else
			{
				Nodo<T> pivote = primero;
				for(int i=0; i < posicion-1; i++)
				{
					pivote = pivote.getSiguiente();
				}				
				pivote.setSiguiente(pivote.getSiguiente().getSiguiente());
				cantidad_de_nodos--;
			}
		}
	}
	
	
	public void eliminar_repetidos(T dato)
	{
		if(!esta_vacia())
		{
			Nodo<T> pivote = primero;
			for(int i=0; i < cantidad_de_nodos; i++)
			{				
				if(pivote.getDato().equals(dato))
				{
					eliminar_al_medio_sin_return(i);
					i--;
				}
				pivote = pivote.getSiguiente();
			}
		}
	}

	public boolean eliminar(T dato)
	{
		if(!esta_vacia())
		{
			Nodo<T> pivote = primero;
			for(int i=0; i < cantidad_de_nodos; i++)
			{				
				if(pivote.getDato().equals(dato))
				{
					if(i == 0)
					{
						primero = primero.getSiguiente();
						cantidad_de_nodos--;
					}
					else if(i == cantidad_de_nodos-1)
					{
						eliminar_al_final();
					}
					else
					{					
						eliminar_al_medio_sin_return(i);
					}
					return true;
				}
				pivote = pivote.getSiguiente();
			}
		}
		return false;
	}
	
	
	public void borrarTodo()
	{
		this.primero = null;
		this.ultimo = null;
		this.cantidad_de_nodos = 0;
	}
	
	public T get_dato_por_indice(int posicion)
	{
		if(posicion > -1 && posicion < cantidad_de_nodos)
		{
			Nodo<T> pivote = primero;
			for(int i=0; i < posicion; i++)
			{
				//System.out.println("pivote es este: " + pivote.getDato());
				pivote = pivote.getSiguiente();
			}
			if(pivote == null)
			{
				Nodo<T> antes_del_pivote = primero;
				for(int i=0; i < posicion-1; i++)
				{
					antes_del_pivote = antes_del_pivote.getSiguiente();
				}
				System.out.println("antes del pivote: " + antes_del_pivote.getDato());
			}
			return pivote.getDato();
		}
		return null;
	}
	
	public T get_dato(T dato)
	{
		if(cantidad_de_nodos != 0)
		{
			Nodo<T> pivote = primero;
						
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
		
	public boolean existe(T dato)
	{
		return get_dato(dato) != null;
	}
	
	public void mostrar(boolean mostrar_estructura)
	{
		if(esta_vacia())
		{
			System.out.print("[->]");
		}
		else
		{
			Nodo<T> pivote = this.primero;
			if(mostrar_estructura)
			{
				for(int i=0; i < cantidad_de_nodos; i++)
				{	
					System.out.print("[" + pivote.getDato().toString() + "]->");				
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
				System.out.print("]");
			}
		}
	}
	public void mostrarln(boolean mostrar_estructura)
	{
		if(esta_vacia())
		{
			System.out.print("[->]");
		}
		else
		{
			Nodo<T> pivote = this.primero;
			if(mostrar_estructura)
			{
				for(int i=0; i < cantidad_de_nodos; i++)
				{	
					System.out.print("[" + pivote.getDato().toString() + "]->");				
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