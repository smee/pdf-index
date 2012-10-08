package pdfindex;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class Search {
  private IndexSearcher searcher = null;
  private QueryParser parser = null;

  public Search(String indexDir) throws IOException {
    searcher = new IndexSearcher(indexDir);
    parser = new MultiFieldQueryParser(new String[]{"body_text","filename"}, new StandardAnalyzer());
  }

  public Hits performSearch(String queryString) 
  throws IOException, ParseException {
    Query query = parser.parse(queryString);
    Hits hits = searcher.search(query);
    return hits;
  }

	public static void main(String[] args) throws IOException, ParseException {
		if (args.length < 2)
			System.err.println("Call with <index dir> <query term>+");
		else {
			Search s = new Search(args[0]);
			Hits hits = s.performSearch(args[1]);
			for (int i = 0; i < hits.length(); i++) {
				System.out.println(hits.doc(i).getField("filename")
						.stringValue());
			}
		}

	}
  
}