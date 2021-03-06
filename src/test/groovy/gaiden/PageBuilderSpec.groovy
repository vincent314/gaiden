/*
 * Copyright 2013 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gaiden

import gaiden.context.PageBuildContext

class PageBuilderSpec extends GaidenSpec {

    def "'build' should build a page"() {
        setup:
        def templateEngine = new TemplateEngine('''
            |<html>
            |<head>
            |    <title>$title</title>
            |</head>
            |<body>
            |$content
            |</body>
            |</html>
            '''.stripMargin())

        and:
        def pageSource = new PageSource(path: "test.md", content: "# Test")
        def documentSource = new DocumentSource(pageSources: [pageSource])
        def context = new PageBuildContext(documentSource: documentSource, toc: new Toc(tocNodes: []))

        when:
        def page = new PageBuilder(templateEngine).build(context, pageSource)

        then:
        page.path == "test.html"
        page.content == '''
            |<html>
            |<head>
            |    <title>Test Title</title>
            |</head>
            |<body>
            |<h1>Test</h1>
            |</body>
            |</html>
            '''.stripMargin()
    }

}
