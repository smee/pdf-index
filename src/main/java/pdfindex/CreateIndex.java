package pdfindex;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;

import com.snowtide.pdf.PDFTextStream;
import com.snowtide.pdf.lucene.DocumentFactoryConfig;
import com.snowtide.pdf.lucene.PDFDocumentFactory;
 
public class CreateIndex {
    /**
     * Simple method that adds the contents of the provided PDF document to the
     * Lucene index via an already-open Lucene IndexWriter.
     */
    public static void addPDFToIndex (IndexWriter openIndex, File pdfFile)
            throws IOException {
        // create and configure new DocumentFactoryConfig instance
        DocumentFactoryConfig config = new DocumentFactoryConfig();
        // set the name to be used for the main body of text extracted from the
        // PDF file, and set it to not be stored, but to be tokenized and indexed
        config.setMainTextFieldName("body_text");
        config.setTextSettings(false, true, true);
        // only copy the PDF metadata attributes into Lucene Document instances
        // produced by PDFDocumentFactory that we explicitly map
        // via DocumentFactoryConfig.setFieldName()
        config.setCopyAllPDFAttrs(false);
        // cause PDF metadata attribute values to be stored, tokenized, and indexed
        config.setPDFAttrSettings(true, true, true);
        // Explicitly set the names that should be used for the fields that are
        // created in the Lucene Document instance -- otherwise, default PDF
        // names will be used that will likely not be picked up when the index
        // is searched.
        // For example, the default name for the modification date
        // field in PDF files is 'ModDate', but our example Lucene index stores
        // the modification dates of Documents with the name 'mod_date'. The
        // third setFieldName() call below establishes the correct mapping.
        config.setFieldName(PDFTextStream.ATTR_AUTHOR, "creator");
        config.setFieldName(PDFTextStream.ATTR_TITLE, "title");
        //config.setFieldName(PDFTextStream.ATTR_CREATION_DATE, "creation_date");
        //config.setFieldName(PDFTextStream.ATTR_MOD_DATE, "mod_date");
        // actually generate the Lucene Document instance from the PDF file
        // using the configuration we've just built, and add the Document to the
        // Lucene index
        Document doc = PDFDocumentFactory.buildPDFDocument(pdfFile, config);
        doc.add(new Field("filename",pdfFile.getAbsoluteFile().toString(),Field.Store.YES, Field.Index.TOKENIZED));
        openIndex.addDocument(doc);
    }

	public static void main(String[] args) throws CorruptIndexException,
			LockObtainFailedException, IOException {
		if (args.length < 2) {
			System.err.println("Call with <index directory> <pdf directory>!");
		} else {
			IndexWriter iw = new IndexWriter(args[0], new StandardAnalyzer());
			addAllPdfsIn(new File(args[1]), iw);
			iw.flush();
			iw.close();
		}
	}
	private static void addAllPdfsIn(File directory, IndexWriter iw) throws IOException {
		for(File f:directory.listFiles()){
			if(f.isDirectory())
				addAllPdfsIn(f, iw);
			else if(f.getName().toLowerCase().endsWith(".pdf")){
				System.out.println("Indexing pdf: "+f);
				addPDFToIndex(iw, f);
			}
		}
		
	}
}