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
 * The control of resource bundle using UTF-8 encoding in resource properties.
 *
 * @author Hideki IGARASHI
 */
class Utf8ResourceBundleControl extends ResourceBundle.Control {

    /**
     * Instantiates a resource bundle.
     * This method is based on {@link ResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     * <p>
     * This implement reads properties files as UTF-8.
     * Also, {@code "java.class"} format is not supported.
     * {@code format} parameter is ignored.
     *
     * @param baseName the base bundle name
     * @param locale the locale for which the resource bundle should be instantiated
     * @param format this parameter is ignored
     * @param loader the {@code ClassLoader} to use to load the bundle
     * @param reload the flag to indicate bundle reloading
     * @return the resource bundle instance
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws InstantiationException if the instantiation of a class fails for some other reason.
     * @throws IOException if an error occurred when reading resources using any I/O operations
     * @see ResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)
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

        try {
            return new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"))
        } finally {
            stream.close()
        }
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
