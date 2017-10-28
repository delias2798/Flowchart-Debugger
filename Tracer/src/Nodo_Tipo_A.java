

public class Nodo_Tipo_A
{
	public String tipo;
	private String statement;
	private String body;
	public Nodo_Tipo_A dentro;
	public Nodo_Tipo_A despues;
	public int nivel;
	
	public Nodo_Tipo_A(){}
	public Nodo_Tipo_A(String dato)
	{
		this.statement = dato;
	}
	public Nodo_Tipo_A(String dato, String body)
	{
		this.statement = dato;
		this.body = body;
	}

	
	public String getStatement(){return statement;}
	public void setStatement(String dato){this.statement = dato;}
	
	public String getBody(){return body;}
	public void setBody(String body){this.body = body;}
	
	public Nodo_Tipo_A getDentro(){return dentro;}
	public void setDentro(Nodo_Tipo_A dentro){this.dentro = dentro;}
	
	public Nodo_Tipo_A getDespues(){return despues;}
	public void setDespues(Nodo_Tipo_A despues){this.despues = despues;}
}
