# pdf-index

A simple app that indexes the full text of all pdfs within one directory as well as allows searching for terms.
Uses the great PDF extraction library [PDFTextStream from SnowTide](http://snowtide.com) 

## Usage
* Build with: `lein uberjar`
* create an index with: `java -jar pdf-index.jar --index <index directory> <pdf directories>+`
* search for any term with: `java -jar pdf-index.jar --search <index directory> <search term>+`

**Beware**: You **MUST NOT** redistribute the resulting jar file (restriction of the pdf library, see [License](http://snowtide.com/buy)).
## License

Copyright © 2012 Steffen Dienst

Distributed under the Eclipse Public License, the same as Clojure.
