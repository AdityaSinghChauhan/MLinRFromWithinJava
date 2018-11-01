package RServe;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RServeConnection {
	
	RConnection rConn = null;
	String ip = null;
	int port = 0;

	public RServeConnection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	private void createRConnection()
	{
		try {
			rConn = new RConnection(this.ip, this.port);
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConnection(){
		rConn.close();
	}
	
	public boolean executeTrainingScript(String script){
		boolean success = true;
		script = script.replace("\\", "\\\\");
		try {
			this.createRConnection();
			script = script.replaceAll("\r\n", "\n");
			String[] linesOfCode = script.split("\n");
			for(int i = 0; i < linesOfCode.length; i++){
				rConn.parseAndEval(linesOfCode[i]);
			}
		} catch (REngineException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}finally{
			this.closeConnection();
		}
		return success;
		
	}
	
	public REXP executePredictionScript(String script){
		boolean success = true;
		REXP result = null;
		script = script.replace("\\", "\\\\");
		try {
			this.createRConnection();
			script = script.replaceAll("\r\n", "\n");
			String[] linesOfCode = script.split("\n");
			for(int i = 0; i < linesOfCode.length; i++){
				result = rConn.parseAndEval(linesOfCode[i]);
			}
		} catch (REngineException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}finally{
			this.closeConnection();
		}
		return result;
		
	}
}
