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

package gaiden.util

/**
 *
 *
 * @author Hideki IGARASHI
 * @author Kazuki YAMAMOTO
 */
class Utf8ResourceBundleControl extends ResourceBundle.Control {

    /**
     * http://stackoverflow.com/a/4660195/1359107
     *
     * @param baseName
     * @param locale
     * @param format
     * @param loader
     * @param reload
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    @Override
    ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
    throws IllegalAccessException, InstantiationException, IOException {
        def bundleName = toBundleName(baseName, locale)
        def resourceName = toResourceName(bundleName, "properties")

        def stream = (reload) ? getReloadableInputStream(loader, resourceName) : loader.getResourceAsStream(resourceName)
        if (!stream) {
            return null
        }

        ResourceBundle bundle = null
        try {
            bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"))
        } finally {
            stream.close()
        }
        bundle
    }

    private InputStream getReloadableInputStream(ClassLoader loader, String resourceName) {
        def url = loader.getResource(resourceName)
        if (!url) {
            return null
        }

        def connection = url.openConnection()
        if (!connection) {
            return null
        }

        connection.setUseCaches(false)
        return connection.inputStream
    }

}
