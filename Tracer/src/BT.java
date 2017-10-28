import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument.Iterator;

public class BT extends Iterator
{
	Nodo_Tipo_A root;
	
	
	public BT(){}
	
	public void setRoot(Nodo_Tipo_A root)
	{
		this.root = root;
	}
	public Nodo_Tipo_A getMin()
	{
		return getMin(root);
	}
	public void insertar(Nodo_Tipo_A nodo_nuevo)
	{
		insertar(root, nodo_nuevo);
	}
	public void insertar(Nodo_Tipo_A nodo, Nodo_Tipo_A nodo_nuevo)
	{		
		nodo = getMax(nodo);
		if(nodo_nuevo.nivel > nodo.nivel)
		{
			if(nodo.getDentro() == null)
			{
				nodo.setDentro(nodo_nuevo);
			}
			else
			{
				insertar(nodo.getDentro(), nodo_nuevo);
			}
		}
		if(nodo_nuevo.nivel == nodo.nivel)
		{
			nodo.setDespues(nodo_nuevo);
		}
		
	}
	
	public Nodo_Tipo_A getMin(Nodo_Tipo_A nodo)
	{
		if(nodo.getDentro() != null)
		{
			return getMin(nodo.getDentro());
		}
		return nodo;
	}
	public Nodo_Tipo_A getMax()
	{
		return getMax(root);
	}
	public Nodo_Tipo_A getMax(Nodo_Tipo_A nodo)
	{
		if(nodo.getDespues() != null)
		{
			return getMax(nodo.getDespues());
		}
		else
		{
			return nodo;
		}
		
	}
	
	public Nodo_Tipo_A getLast()
	{
		return getMax(getMin(root));
	}
	public void print()
	{
		print(root);
	}
	private void print(Nodo_Tipo_A nodo)
	{
		if(nodo != null)
		{
			System.out.println(nodo.getStatement() + nodo.nivel);
			print(nodo.getDentro());
			print(nodo.getDespues());
		}
	}

	@Override
	public AttributeSet getAttributes()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEndOffset()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartOffset()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Tag getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void next()
	{
		// TODO Auto-generated method stub
		
	}
}
