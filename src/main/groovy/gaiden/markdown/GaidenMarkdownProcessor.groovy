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

package gaiden.markdown

import gaiden.Holders
import gaiden.PageSource
import gaiden.context.PageBuildContext
import org.pegdown.ParsingTimeoutException
import org.pegdown.PegDownProcessor
import org.pegdown.plugins.PegDownPlugins
import org.vince.pegdown.furigana.FuriganaParser

/**
 * A Processor for Markdown.
 *
 * @author Hideki IGARASHI
 * @author Kazuki YAMAMOTO
 */
class GaidenMarkdownProcessor extends PegDownProcessor {

    GaidenMarkdownProcessor(int options) {
        super(options,buildPlugins())
    }

    static PegDownPlugins buildPlugins() {
//        String[] plugins = Holders.config.pegdownPlugins
//
//        if(!plugins) {
//            return PegDownPlugins.NONE
//        }

        PegDownPlugins.Builder builder = PegDownPlugins.builder()
//        plugins.each {
//            builder.withPlugin(Class.forName(it,true,FuriganaParser.class.classLoader))
//        }
        builder.withPlugin(FuriganaParser.class)
        builder.build()
    }

    /**
     * Converts the given markdown source to HTML.
     *
     * @param context the context to be built
     * @param pageSource the page source to convert
     * @return the HTML
     * @throws ParsingTimeoutException if the input cannot be parsed within the configured parsing timeout
     */
    String markdownToHtml(PageBuildContext context, PageSource pageSource) throws ParsingTimeoutException {
        def astRoot = parseMarkdown(pageSource.content.toCharArray())
        new GaidenToHtmlSerializer(new GaidenLinkRenderer(context, pageSource), new ImageRenderer(pageSource)).toHtml(astRoot)
    }

}
