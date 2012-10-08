package pdfindex;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		if(args.length<3)
			System.err.println("Call with: [--index|--search] <index directory> (<pdf directory>+|<query term>+)");
		else {
			String[] newArgs = new String[args.length-1];
			System.arraycopy(args, 1, newArgs, 0, newArgs.length);
			
			if("--index".equals(args[0]))
				CreateIndex.main(newArgs);
			else
				Search.main(newArgs);
		}
	}

}
