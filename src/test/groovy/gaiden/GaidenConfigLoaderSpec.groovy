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

import gaiden.message.MessageSource
import spock.lang.Specification

class GaidenConfigLoaderSpec extends Specification {

    def "'initialize' should initialize a configuration file"() {
        setup:
        def initializer = new GaidenConfigLoader()

        when:
        def config = initializer.load(new File("src/test/resources/config/ValidConfig.groovy"))

        then:
        with(config) {
            title == "Test Title"
            tocTitle == "Test TOC Title"
            templateFilePath == "test/templates/layout.html"
            singleTemplateFilePath == "test/templates/single-layout.html"
            tocFilePath == "test/pages/toc.groovy"
            tocOutputFilePath == "test/toc.html"
            pagesDirectoryPath == "test/pages"
            staticDirectoryPath == "test/static"
            outputDirectoryPath == "test/build/html"
            inputEncoding == "UTF-8"
            outputEncoding == "UTF-8"
        }
    }

    def "'initialize' should initialize the default configuration file"() {
        setup:
        def initializer = new GaidenConfigLoader()

        when:
        def config = initializer.load(new File("src/test/resources/config/NotFoundConfig.groovy"))

        then:
        with(config) {
            title == "Gaiden"
            tocTitle == "Table of contents"
            templateFilePath == "templates/layout.html"
            singleTemplateFilePath == "templates/single-layout.html"
            tocFilePath == "pages/toc.groovy"
            tocOutputFilePath == "toc.html"
            pagesDirectoryPath == "pages"
            staticDirectoryPath == "static"
            outputDirectoryPath == "build"
            inputEncoding == "UTF-8"
            outputEncoding == "UTF-8"
        }
    }

    def "'initialize' should initialize the defaults configuration other than values set"() {
        setup:
        def initializer = new GaidenConfigLoader()

        when:
        def config = initializer.load(new File("src/test/resources/config/OnlyTitleConfig.groovy"))

        then:
        with(config) {
            title == "Test Title"
            tocTitle == "Table of contents"
            templateFilePath == "templates/layout.html"
            tocFilePath == "pages/toc.groovy"
            tocOutputFilePath == "toc.html"
            pagesDirectoryPath == "pages"
            staticDirectoryPath == "static"
            outputDirectoryPath == "build"
            inputEncoding == "UTF-8"
            outputEncoding == "UTF-8"
        }
    }

    def "'initialize' should initialize a configuration file with deprecated parameters"() {
        setup:
        def initializer = new GaidenConfigLoader()

        and:
        def savedSystemErr = System.err
        def printStream = Mock(PrintStream)
        System.err = printStream

        and:
        def messageSource = Stub(MessageSource)
        messageSource.getMessage("config.deprecated.parameter.message", _ as List) >> "Test Message"
        Holders.messageSource = messageSource

        when:
        def config = initializer.load(new File("src/test/resources/config/DeprecatedConfig.groovy"))

        then:
        with(config) {
            title == "Gaiden"
            tocTitle == "Table of contents"
            templateFilePath == "templates/deprecated.html"
            tocFilePath == "pages/deprecated-toc.groovy"
            tocOutputFilePath == "deprecated-toc.html"
            pagesDirectoryPath == "deprecated-pages"
            staticDirectoryPath == "deprecated-static"
            outputDirectoryPath == "deprecated-build"
            inputEncoding == "UTF-8"
            outputEncoding == "UTF-8"
        }

        and:
        6 * printStream.println("WARNING: Test Message")

        cleanup:
        System.err = savedSystemErr
    }

}
