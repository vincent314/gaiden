package gaiden

import gaiden.context.BuildContext
import spock.lang.Specification

/**
 * Created by vincent on 08/10/14.
 */
class SingleDocumentWriterSpec extends Specification {

    def outputDirectory = new File("build/gaiden-test-doc")

    def setup() {
        outputDirectory.deleteDir()
    }

    def 'should write all pages to a single file'() {
        setup:
        def page1 = createPage("page1.md")
        def page2 = createPage("page2.md")
        DocumentSource documentSource = new DocumentSource(pageSources: [page1, page2])

        and:
        def toc = createToc()
        BuildContext context = new BuildContext(documentSource: documentSource)

        and:
        def document = new Document(pages: [page1, page2], toc: toc)
        def writer = new SingleDocumentWriter(new File('src/test/resources/templates/single-page-content.html'))

        when:
        writer.write(new File("single.html", outputDirectory), document)

        then:
        new File("single.html", outputDirectory).text == """<html>
<head>
    <title>any title</title>
</head>
<body>
<p>page1.html</p>
<p>page2.html</p>
</body>
</html>
"""
    }

    private Page createPage(String path, String content = null) {
        def source = new PageSource(path: path)
        new Page(source: source, content: content ?: "<p>${source.outputPath}</p>")
    }

    private Toc createToc() {
        new Toc(path: "toc.html", content: "<h1>table of contents</h1>", tocNodes: [
                new TocNode(pageSource: new PageSource(path: "page1.md")),
                new TocNode(pageSource: new PageSource(path: "page2.md"))
        ])
    }
}
