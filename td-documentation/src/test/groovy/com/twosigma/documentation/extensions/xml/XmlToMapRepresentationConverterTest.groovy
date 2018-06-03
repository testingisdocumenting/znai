package com.twosigma.documentation.extensions.xml

import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

class XmlToMapRepresentationConverterTest {
    @Test
    void "converts children and attributes to their own map entries"() {
        def asMap = XmlToMapRepresentationConverter.convert("""
<ul id="menu" class="test">
   <li id="menu-1" class="item">first</li>
   <li id="menu-2" class="item">second</li>
</ul>
""")

        actual(asMap).should equal([
                tagName: 'ul', attributes: [[name: 'id', value: '"menu"'], [name: 'class', value: '"test"']],
                children: [[tagName: 'li', attributes: [[name: 'id', value: '"menu-1"'], [name: 'class', value: '"item"']], children:[
                        [tagName: '', text: 'first']]],
                           [tagName: 'li', attributes: [[name: 'id', value: '"menu-2"'], [name: 'class', value: '"item"']], children:[
                                   [tagName: '', text: 'second']
                           ]]]])
    }
}
