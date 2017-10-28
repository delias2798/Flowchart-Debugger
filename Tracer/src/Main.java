
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.core.ITypeRoot;
//JDT y Runtime
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.sun.jdi.AbsentInformationException;

//Tools
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ClassNotPreparedException;
import com.sun.jdi.Field;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.ClassUnloadRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;


public class Main
{ 
	public static Lista_Doble<BT> LISTA = new Lista_Doble<>();
	public static BT BT = new BT(); 
	public static VirtualMachine vm;
	private static final String[] excludes = { "java.*", "javax.*", "sun.*", "com.sun.*" };
	private static boolean connected = true;
	private static boolean vmDied;
	private static ShowCode showCode;
	private static String IN_DEBUGGER = "TraceDebuggingVMHasLaunched";	
	protected static ArrayList<String> PREFIX_ARGS_FOR_VM;
	static
	{
		PREFIX_ARGS_FOR_VM = new ArrayList<> ();//////////////////////////////////////////////////////////////////////////////////////////////////////
		PREFIX_ARGS_FOR_VM.add ("-cp");
		PREFIX_ARGS_FOR_VM.add ("\"" + System.getProperty("java.class.path") + "\"");
		
//		System.out.println("A");
//		System.out.println("\"" + System.getProperty("java.class.path") + "\"");
//		System.out.println("B");
		
		PREFIX_ARGS_FOR_VM.add ("-D" + IN_DEBUGGER + "=true");
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException, InterruptedException
	{
//		vm = inicializacion(args, true);
//		showCode = new ShowCode();
//		setEventRequests();
//		run();		
		//vm.resume();
		
		BT.setRoot(new Nodo_Tipo_A("ROOT"));		
		ParseFilesInDir("Dashboard");
		
//		BT.setRoot(new Nodo_Tipo_A("ROOT"));
//		
//		Nodo_Tipo_A n1 = new Nodo_Tipo_A();
//		n1.nivel = 8;
//		n1.setStatement("A");
//		BT.insertar(n1);
//
//		Nodo_Tipo_A n2 = new Nodo_Tipo_A();
//		n2.nivel = 8;
//		n2.setStatement("B");
//		BT.insertar(n2);
//
//		BT.print();

	}
	
	
	private static VirtualMachine inicializacion(String[] args, boolean terminateAfter)
	{		
		int length = PREFIX_ARGS_FOR_VM.size();
		String[] allArgs = new String[length + args.length + 1];
		for(int i = 0; i < length; i++)
		{
			allArgs[i] = PREFIX_ARGS_FOR_VM.get(i);
		}
		allArgs[length] = "Main";
		System.arraycopy(args, 0, allArgs, length + 1, args.length);		
		return launchConnect(allArgs);
		
	}
	// Set up a launching connection to the JVM
	private static VirtualMachine launchConnect(String[] args)
	{
		VirtualMachine vm = null;
		LaunchingConnector conn = getCommandLineConnector();
		Map<String, Connector.Argument> connArgs = setMainArgs(conn, args);
		try
		{
			vm = conn.launch(connArgs); // launch the JVM and connect to it			
		}
		catch(IOException e)						{throw new Error("\n!!! Unable to launch JVM: " + e);}
		catch(IllegalConnectorArgumentsException e)	{throw new Error("\n!!! Internal error: " + e);}
		catch(VMStartException e)					{throw new Error("\n!!! JVM failed to start: " + e.getMessage ());}
		return vm;
	}
	// find a command line launch connector
	private static LaunchingConnector getCommandLineConnector ()
	{
		List<Connector> conns = Bootstrap.virtualMachineManager ().allConnectors ();
		for (Connector conn : conns)
		{
			if(conn.name ().equals ("com.sun.jdi.CommandLineLaunch"))
			{
				return (LaunchingConnector) conn;
			}			
		}
		throw new Error ("\n!!! No VM launching connector found");
	}
	
	// make the tracer's input arguments the program's main() arguments
	private static Map<String, Connector.Argument> setMainArgs(LaunchingConnector conn, String[] args)
	{
		// get the connector argument for the program's main() method
		Map<String, Connector.Argument> connArgs = conn.defaultArguments();
		Connector.Argument mArgs = connArgs.get("main");
		if (mArgs == null)
		{
			throw new Error ("\n!!!! Bad launching connector");
		}

		// concatenate all the tracer's input arguments into a single string
		StringBuffer sb = new StringBuffer ();
		for(int i = 0; i < args.length; i++)
		{
			System.out.println(args[i] + " ");
			sb.append(args[i] + " ");
		}
		mArgs.setValue(sb.toString ()); // assign input args to application's main()
		return connArgs;
	}
	
	
	public static void addPrefixOptionsForVm(String value){ PREFIX_ARGS_FOR_VM.add (value); }	
	
	
	
	
	private static void setEventRequests()
	{
		EventRequestManager mgr = vm.eventRequestManager();
		MethodEntryRequest menr = mgr.createMethodEntryRequest();
		for(int i = 0; i < excludes.length; ++i) // report method entries
		{
			menr.addClassExclusionFilter(excludes[i]);
		}
		menr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		menr.enable();
		
		
		MethodExitRequest mexr = mgr.createMethodExitRequest();
		for(int i = 0; i < excludes.length; ++i) // report method exits
			mexr.addClassExclusionFilter(excludes[i]);
		mexr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		mexr.enable();
		
		
		ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
		for(int i = 0; i < excludes.length; ++i) // report class loads
		{
			cpr.addClassExclusionFilter(excludes[i]);
		}
		cpr.enable();
		
		
		ClassUnloadRequest cur = mgr.createClassUnloadRequest();
		for(int i = 0; i < excludes.length; ++i) // report class unloads
		{
			cur.addClassExclusionFilter(excludes[i]);
		}
		cur.enable();
		
		
		ThreadStartRequest tsr = mgr.createThreadStartRequest();
		tsr.enable(); // report thread starts
		
		
		ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
		tdr.enable(); // report thread deaths
		
	} // end of setEventRequests()
	
	
	public static void run()
	{
		//TestStack.main(new String[0]);//////////////////////////////////////////////////////////////////////////////////////////////////
		EventQueue queue = vm.eventQueue();
		while(connected)
		{
			try
			{
				EventSet eventSet = queue.remove();
				for(Event event : eventSet)
				{
					handleEvent(event);
				}
				eventSet.resume();
			}
			catch(InterruptedException e){} // Ignore
			catch(VMDisconnectedException discExc)
			{
				handleDisconnectedException();
				break;
			}
		}
	} // end of run()
	
	private static void handleEvent(Event event)
	{
		// method events
		if (event instanceof MethodEntryEvent)
		{
			//System.out.println("MethodEntryEvent");
			methodEntryEvent((MethodEntryEvent) event);
		}
		
		else if(event instanceof MethodExitEvent)
		{
			//System.out.println("MethodExitEvent");
			methodExitEvent((MethodExitEvent) event);
		}
		
		// class events
		else if(event instanceof ClassPrepareEvent)
		{
			//System.out.println("ClassPrepareEvent");
			classPrepareEvent((ClassPrepareEvent) event);
		}
		
		else if(event instanceof ClassUnloadEvent)
		{
			//System.out.println("ClassUnloadEvent");
			classUnloadEvent((ClassUnloadEvent) event);
		}
		
		// thread events
		else if(event instanceof ThreadStartEvent)
		{
			//System.out.println("ThreadStartEvent");
			//threadStartEvent((ThreadStartEvent) event);
		}
		
		else if(event instanceof ThreadDeathEvent)
		{
			//System.out.println("ThreadDeathEvent");
			//threadDeathEvent((ThreadDeathEvent) event);
		}
		
		// step event -- a line of code is about to be executed
		else if(event instanceof StepEvent)
		{
			//System.out.println("StepEvent");
			stepEvent((StepEvent) event);
		}
		
		// modified field event -- a field is about to be changed
		else if (event instanceof ModificationWatchpointEvent)
		{
			//System.out.println("ModificationWatchpointEvent");
			fieldWatchEvent((ModificationWatchpointEvent) event);
		}
		
		// VM events
		else if(event instanceof VMStartEvent)
		{
			System.out.println("VMStartEvent");
			//vmStartEvent((VMStarEvent) event);
		}
		else if(event instanceof VMDeathEvent)
		{
			System.out.println("VMDeathEvent");
			vmDeathEvent((VMDeathEvent) event);
		}
		else if (event instanceof VMDisconnectEvent)
		{
			System.out.println("VMDisconnectEvent");
			vmDisconnectEvent((VMDisconnectEvent) event);
		}
		
		else
		{
			throw new Error("Unexpected event type");
		}
		
	} // end of handleEvent()

	private static void methodEntryEvent(MethodEntryEvent event)// entered a method but no code executed yet
	{
		Method meth = event.method();
		
		String className = meth.declaringType().name();
		System.out.println();
		if(meth.isConstructor())
		{
			System.out.println("entered " + className + " constructor");
		}			
		else
		{
			System.out.println("entered " + className + "."+meth.name()+"()");
		}
	} // end of methodEntryEvent()
	
	private static void methodExitEvent(MethodExitEvent event)// all code in method has been executed, and about to return
	{
		Method meth = event.method();
		String className = meth.declaringType().name();
		if(meth.isConstructor())
		{
			System.out.println("exiting " + className + " constructor");
		}
		else
		{
			System.out.println("exiting " + className+"."+meth.name()+"()");
		}
		System.out.println();
	}
	
	private static void classUnloadEvent(ClassUnloadEvent event)
	{
		if(!vmDied)
		{
			System.out.println("unloaded class: " + event.className());
		}
	}

	private static void classPrepareEvent(ClassPrepareEvent event)// a new class has been loaded
	{
		ReferenceType classReference = event.referenceType();
		List<Field> fields = classReference.fields();
		List<Method> methodos = classReference.methods();
		String classSourceName;
		try
		{
			classSourceName = classReference.sourceName(); // get filename of the class
			System.out.println("classSourceName: " + classSourceName);
			showCode.add(classSourceName);
		}
		catch(AbsentInformationException e)
		{
			classSourceName = "??";
		}
		System.out.println("loaded class: " + classReference.name()+" from " + classSourceName + " - fields=" + fields.size() + ", methods=" + methodos.size() );
		System.out.println(" fields names: ");
		for(Field field : fields)
		{
			System.out.println(" | " + field.typeName() + " " + field.name());
		}
		System.out.println();
		System.out.println(" method names: ");
		for(Method metodo : methodos)
		{
			System.out.println(" | " + metodo.name() + "()" );
		}
		setFieldsWatch(fields);
	} // end of classPrepareEvent()

	
	
	private static void stepEvent(StepEvent event)
	{
		Location loc = event.location();
		try// print the line
		{ 
			String fnm = loc.sourceName(); // get code's filename
			System.out.println(fnm + ": " + showCode.show(fnm, loc.lineNumber()) );
		}
		catch(AbsentInformationException e)
		{
			
		}
		if(loc.codeIndex() == 0)
		{
			printInitialState( event.thread() ); // at the start of a method
		}
	} // end of stepEvent()
	
	private static void printInitialState(ThreadReference thr)
	{
	 // get top-most stack frame
		StackFrame currFrame = null;
		try
		{
			currFrame = thr.frame(0);
		}
		catch (Exception e)
		{
			return;
		}
		printVariablesLocales(currFrame); // print fields for the 'this' object
		
		ObjectReference objRef = currFrame.thisObject();
		if(objRef != null)
		{
			System.out.println(" object: " + objRef.toString());
			printFields(objRef);
		}
	} // end of printInitialState()
	
	
	private static void printVariablesLocales(StackFrame currFrame)
	{
		List<LocalVariable> variablesLocales = null;
		try
		{
			variablesLocales = currFrame.visibleVariables();
		}
		catch (Exception e)
		{
			return;
		}
		if(variablesLocales.size() == 0) // no local vars in the list
		{
			return;
		}
		System.out.println(" Variables locales: ");
		for(LocalVariable variableLocal : variablesLocales)
		{
			System.out.println(" |(vl) " + variableLocal.name() + " = " + currFrame.getValue(variableLocal) );
		}
	} // end of printLocals()

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void printFields(ObjectReference objectReference)
	{
		ReferenceType referenceType = objectReference.referenceType(); // get class of object
		List<Field> fields = null;
		try
		{
			fields = referenceType.fields(); // get fields from the class
		}
		catch(ClassNotPreparedException e)
		{
			return;
		}
		System.out.println("\tFields: ");
		for(Field f : fields) // print field name and value
		{
			System.out.println(" | " + f.name() + " = " + objectReference.getValue(f) );
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void setFieldsWatch(List<Field> fields)
	{
		EventRequestManager eventRequestManager = vm.eventRequestManager();
		for(Field field : fields)
		{
			ModificationWatchpointRequest modificationWatchPointRequest = eventRequestManager.createModificationWatchpointRequest(field);
			for(int i = 0; i < excludes.length; i++)
			{
				modificationWatchPointRequest.addClassExclusionFilter(excludes[i]);
			}
			modificationWatchPointRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			modificationWatchPointRequest.enable();
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void fieldWatchEvent(ModificationWatchpointEvent event)
	{	
		Field field = event.field();
		Value oldValue = event.valueCurrent();
		Value newValue = event.valueToBe();
		System.out.println(" field '" + field.name() + "' change from " + oldValue + " >> " + newValue);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void setStepping(ThreadReference thr)// start single stepping through the new thread
	{
		EventRequestManager mgr = vm.eventRequestManager();
		StepRequest sr = mgr.createStepRequest(thr, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
		sr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		for(int i = 0; i < excludes.length; ++i)
		{
			sr.addClassExclusionFilter(excludes[i]);
		}
		sr.enable();
	} // end of setStepping()

	
	private synchronized static void handleDisconnectedException()
	{
		EventQueue queue = vm.eventQueue();
		while(connected)
		{
			try
			{
				EventSet eventSet = queue.remove();
				for(Event event : eventSet)
				{
					if(event instanceof VMDeathEvent)
					{
						vmDeathEvent((VMDeathEvent) event);
					}
					else if(event instanceof VMDisconnectEvent)
					{
						vmDisconnectEvent((VMDisconnectEvent) event);
					}
				}
				eventSet.resume();
			}
			catch (InterruptedException e) { } // ignore
		}
	} // end of handleDisconnectedException()
	
	private static void vmDeathEvent(VMDeathEvent event)// Notification of VM termination
	{
		vmDied = true;
		System.out.println("-- The application has exited --");
	}
	private static void vmDisconnectEvent(VMDisconnectEvent event)/* Notification of VM disconnection, either through normal termination or because of an exception/error. */
	{
		connected = false;
		if(!vmDied)
		{
			System.out.println("- The application has been disconnected -");	 
		}
	}
	
	
	
	
	
	private static void addClassWatch(VirtualMachine vm)
	{
		EventRequestManager erm = vm.eventRequestManager();
		ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
		classPrepareRequest.addClassFilter("Stack");
		classPrepareRequest.setEnabled(true);
	}

	
	private static void addFieldWatch(VirtualMachine vm,ReferenceType refType)
	{
		EventRequestManager erm = vm.eventRequestManager();
		Field field = refType.fieldByName("store");
		ModificationWatchpointRequest modificationWatchpointRequest = erm.createModificationWatchpointRequest(field);
		modificationWatchpointRequest.setEnabled(true);
		
		
//		Field field2 = refType.fieldByName(FIELD_NAME_2);
//		ModificationWatchpointRequest modificationWatchpointRequest2 = erm.createModificationWatchpointRequest(field2);
//		modificationWatchpointRequest2.setEnabled(true);
	}
	
	//use ASTParse to parse string
	public static void parse(String str)
	{
		System.out.println(str);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit)parser.createAST(null);		
		
		
		
		cu.accept(new ASTVisitor()
		{
			
			public boolean visit(org.eclipse.jdt.core.dom.FieldDeclaration fieldDeclaration)//Declaracion de Metodo
			{//Declaraciones
				System.out.println("Declaracion de Atributo '" + fieldDeclaration.toString() + "' en linea: " + cu.getLineNumber(fieldDeclaration.getStartPosition()));
				return true; // continue 
			}
			public boolean visit(VariableDeclarationFragment variableDeclarationFragment)
			{//Declaraciones
				System.out.println("Declaracion de Variable '" + variableDeclarationFragment.getName() + "' en linea: " + cu.getLineNumber(variableDeclarationFragment.getStartPosition()));				
				return true; // do not continue 
			}
			public boolean visit(org.eclipse.jdt.core.dom.Assignment assignment)//Declaracion de Metodo
			{//Asignaciones
				System.out.println("Asignacion '" + assignment.toString() + "' en linea: " + cu.getLineNumber(assignment.getStartPosition()));				
				return true; // continue 
			}	
			public boolean visit(MethodDeclaration methodDeclaration)//Declaracion de Metodo
			{//Declaraciones				

				Nodo_Tipo_A n = new Nodo_Tipo_A();
				n.tipo = "metodo";
				n.setStatement("Metodo");
				n.nivel = 1;
				BT.insertar(n);
				
				
				System.out.println("Declaracion del Metodo '" + methodDeclaration.getName() + "()' en linea: " + cu.getLineNumber(methodDeclaration.getStartPosition()));
				System.out.println("Body:\n" + methodDeclaration.getBody());/////////////////////////////////////////////////////////////////////////////////
				//BT.getMin().setDentro(new Nodo_Tipo_A(methodDeclaration.getName().toString()));
				
				methodDeclaration.accept(new ASTVisitor()
				{
					
					public boolean visit(org.eclipse.jdt.core.dom.IfStatement ifStatement)
					{						
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + methodDeclaration.getName());
//						System.out.println("If '" + ifStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(ifStatement.getStartPosition()));
//						System.out.println("If Body:\n" + ifStatement.getThenStatement().toString());/////////////////////////////////////////////////////////////////////////////////
						System.out.println("if(" + ifStatement.getExpression() + ")");
						Nodo_Tipo_A n = new Nodo_Tipo_A();
						n.tipo = "if";
						n.setStatement("if(" + ifStatement.getExpression() + ")");
						n.setBody(ifStatement.getThenStatement().toString());
						n.nivel = cu.getColumnNumber(ifStatement.getStartPosition());
						BT.insertar(n);
						//System.out.println("MAGIA: "+cu.getColumnNumber(ifStatement.getStartPosition())+" OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
						
						return true; // continue 
					}
					public boolean visit(org.eclipse.jdt.core.dom.WhileStatement whileStatement)
					{
						
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + methodDeclaration.getName());
//						System.out.println("While '" + whileStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(whileStatement.getStartPosition()));
//						System.out.println("While Body:\n" + whileStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
						System.out.println("while(" + whileStatement.getExpression() + ")");
						
						Nodo_Tipo_A n = new Nodo_Tipo_A();
						n.tipo = "while";
						n.setStatement("while(" + whileStatement.getExpression() + ")");
						n.setBody(whileStatement.getBody().toString());
						n.nivel = cu.getColumnNumber(whileStatement.getStartPosition());
						BT.insertar(n);
						
						//System.out.println("MAGIA: "+cu.getColumnNumber(whileStatement.getStartPosition())+" OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
						return true;
					}
					public boolean visit(org.eclipse.jdt.core.dom.DoStatement doStatement)
					{
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + methodDeclaration.getName());
						//System.out.println("Do While '" + doStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(doStatement.getStartPosition()));
						//System.out.println("Do While Body:\n" + doStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
						System.out.println("do while(" + doStatement.getExpression() + ")");
						
						Nodo_Tipo_A n = new Nodo_Tipo_A();
						n.tipo = "do while";
						n.setStatement("do while(" + doStatement.getExpression() + ")");
						n.setBody(doStatement.getBody().toString());
						n.nivel = cu.getColumnNumber(doStatement.getStartPosition());
						BT.insertar(n);
						
						//System.out.println("MAGIA: "+cu.getColumnNumber(doStatement.getStartPosition())+" OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
						return true; // continue 
					}						
					public boolean visit(org.eclipse.jdt.core.dom.ForStatement forStatement)
					{
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + methodDeclaration.getName());
						//System.out.println("For '" + forStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(forStatement.getStartPosition()));
						//System.out.println("For Body:\n" + forStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
						System.out.println("for(" + forStatement.getExpression() + ")");
						
//						Nodo_Tipo_A n = new Nodo_Tipo_A();
//						n.tipo = "for";
//						n.setStatement("for(" + forStatement.getExpression() + ")");
//						n.setBody(forStatement.getBody().toString());
//						n.nivel = cu.getColumnNumber(forStatement.getStartPosition());
//						BT.insertar(n);
						
						//System.out.println("MAGIA: "+cu.getColumnNumber(forStatement.getStartPosition())+" OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
						return true; // continue 
					}					
				});
				
				return true; // continue 
			}
			public boolean visit(org.eclipse.jdt.core.dom.MethodInvocation methodInvocation)//Metodo Externo
			{
				System.out.println("Metodo(Interno) '" + methodInvocation.getName() + "' en linea: " + cu.getLineNumber(methodInvocation.getStartPosition()));
				System.out.println("getExpression(): " + methodInvocation.getExpression());
				
				
				return true; // continue 
			}
			public boolean visit(org.eclipse.jdt.core.dom.ExpressionStatement expressionStatement)//Metodo interno
			{				
				
				System.out.println("Metodo(Externo) '" + expressionStatement.toString() + "' en linea: " + cu.getLineNumber(expressionStatement.getStartPosition()));
				return true; // continue 
			}
//			public boolean visit(org.eclipse.jdt.core.dom.IfStatement ifStatement)
//			{
//				System.out.println("If '" + ifStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(ifStatement.getStartPosition()));
//				System.out.println("If Body:\n" + ifStatement.getThenStatement().toString());/////////////////////////////////////////////////////////////////////////////////
//				return true; // continue 
//			}
//			public boolean visit(org.eclipse.jdt.core.dom.WhileStatement whileStatement)
//			{
//				System.out.println("While '" + whileStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(whileStatement.getStartPosition()));
//				System.out.println("While Body:\n" + whileStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//				
//								
//				String codeSection = "while("+whileStatement.getExpression()+")\n"+whileStatement.getBody();
//				System.out.println(codeSection);
//
//				
//				output.insertar_al_final(parseSection(whileStatement));
//				
//				
//				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//				return true; // continue 
//			}
//			public boolean visit(org.eclipse.jdt.core.dom.DoStatement doStatement)
//			{
//				System.out.println("Do While '" + doStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(doStatement.getStartPosition()));
//				System.out.println("Do While Body:\n" + doStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
//				return true; // continue 
//			}						
//			public boolean visit(org.eclipse.jdt.core.dom.ForStatement forStatement)
//			{
//				System.out.println("For '" + forStatement.getExpression().toString() + "' en linea: " + cu.getLineNumber(forStatement.getStartPosition()));
//				System.out.println("For Body:\n" + forStatement.getBody().toString());/////////////////////////////////////////////////////////////////////////////////
//				return true; // continue 
//			}
		});
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		BT.print();
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(BT.root.getDentro().getDespues().getDespues().getDentro().getDentro().getDespues().getDespues().tipo);
	}

	
	
	
 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException
	{
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1)
		{
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		} 
		reader.close();
		return  fileData.toString();
	}
 
	
	//loop directory to get file list
	public static void ParseFilesInDir(String nombreDelProyecto) throws IOException
	{
		//File dirs = new File(".");
		//String selfPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
		//System.out.println("selfPath: " + selfPath);
		
		String pathX = "C:\\Users\\aleja\\Documents\\Tec\\2017\\S2\\Datos I\\Codigo\\varas_de_eclipse\\" + nombreDelProyecto + File.separator+"src"+File.separator;
		System.out.println("pathX: " + pathX);
		
		File root = new File(pathX);
		
		Lista_Simple<File> files = new Lista_Simple<>();
		File[] fs = root.listFiles();
		for(int i=0; i < fs.length; i++)
		{
			files.insertar_al_final(fs[i]);
		}

		String filePath = null;
 
		 for(int i=0; i < files.getCantidad_de_nodos(); i++)
		 {
			 File f = files.get_dato_por_indice(i);
			 filePath = f.getAbsolutePath();
			 if(f.isFile())
			 {
				 System.out.println("____________________________________________________________________________________________________________________");
				 System.out.println("Parsiando -> " + filePath);
				 System.out.println("____________________________________________________________________________________________________________________");
				 parse(readFileToString(filePath));
			 }
			 else
			 {
				 Lista_Simple<File> sfiles = new Lista_Simple<>();
				 File[] sfs = f.listFiles();
				 for(int j=0; j < sfs.length; j++)
				 {
					 sfiles.insertar_al_final(sfs[j]);
				 }				 
				 
				 for(int k=0; k < sfiles.getCantidad_de_nodos(); k++)
				 {
					 File sf = sfiles.get_dato_por_indice(k);
					 filePath = sf.getAbsolutePath();
					 if(sf.isFile())
					 {
						 System.out.println("____________________________________________________________________________________________________________________");
						 System.out.println("Parsiando -> " + filePath);
						 System.out.println("____________________________________________________________________________________________________________________");
						 parse(readFileToString(filePath));
					 }
				 }
			 }
		 }
		 
	}
		
	
}