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
        // The below is a copy of the default implementation.
        String bundleName = toBundleName(baseName, locale)
        String resourceName = toResourceName(bundleName, "properties")
        ResourceBundle bundle = null
        InputStream stream = null
        if (reload) {
            URL url = loader.getResource(resourceName)
            if (url != null) {
                URLConnection connection = url.openConnection()
                if (connection != null) {
                    connection.setUseCaches(false)
                    stream = connection.inputStream
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName)
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as UTF-8.
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"))
            } finally {
                stream.close()
            }
        }
        return bundle
    }

}
