package gaiden

/**
 * Writer to create a single document from all files
 *
 * Created by vincent on 08/10/14.
 */
class SingleDocumentWriter {

    TemplateEngine templateEngine

    SingleDocumentWriter(File template) {
        this.templateEngine = new TemplateEngine(template.text)

    }

    /**
     * Write document HTML content to the output file
     * @param outputFile
     * @param document
     */
    void write(File outputFile, Document document) {
        // Create file for writing if not exist
        if (!outputFile.exists()) {
            def parent = outputFile.parentFile
            if (!parent.exists()) {
                assert parent.mkdirs()
            }
            assert outputFile.createNewFile()
        }

        def content = new StringWriter()
//        printToc(content, document)
        printPages(content, document)


        def binding = [content: content.toString(), title: Holders.config.title]
        outputFile << templateEngine.make(binding)
    }

    /**
     * Write TOC content to the output
     *
     * @param out
     * @param document
     * @return
     */
    def printToc(Writer out, Document document) {
        out << document.toc.content
        out << '\n'
    }

    /**
     * Write all pages content to the output
     *
     * @param out
     * @param document
     * @return
     */
    def printPages(Writer out, Document document) {
        out << getContents(document).join('\n')
    }

    /**
     * Get the final document content in HTML format
     * @param document
     * @return
     */
    List<String> getContents(Document document) {
        def groupBy = document.pages.groupBy { it.source.path }

        return document.toc.tocNodes.collect { TocNode node ->
            groupBy.get(node.pageSource.path)[0].content
        }
    }
}
