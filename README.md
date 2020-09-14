## BitteUnterschreiben *('Please sign')*

*BU* is a simple app to add bitmap images over any .pdf file. Primary usage - adding a facsimile, a picture of a 
handwritten autograph or a print of a rubber stamp onto some kind of document. The app is made for lazy ppl, who have to 
sign many documents a day and tired of printing and scanning their documents and forms. In other words this app saves 
trees, cuts emissions, protects environment, etc.

As a side-feature, *BitteUnterschreiben* is capable of merging multiple .pdf files, extracting and reordering pages.

*BU* has a blending mode option ('Darken' or 'Multiply' or 'No-op') for overlaying images and size/quality settings 
(select target dpi and jpg compression).

*BU* relies upon [Apache PDFBox 2](https://pdfbox.apache.org), an open-source PDF library in terms of rendering and 
saving. Other technologies used: Logback for logging, Jackson for .yml processing and JUnit+Mockito for testing.

In a compiled as a single .jar file state *BitteUnterschreiben* weighs around 7.5 Mb. 

For the current version, user is required to use only .png overlay images scanned in 300 dpi; transparency is desirable 
but not required (please see the examples in 'PNG' directory). 
