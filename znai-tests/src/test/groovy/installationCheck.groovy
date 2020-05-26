/*
 * Copyright 2020 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def unzippedZnai = createLazyResource('unzipped znai') {
    def unzipDest = fs.tempDir('znai-dist')
    fs.unzip('../../../../znai-dist/target/dist-znai.zip', unzipDest)

    return [bin: 'sh ' + unzipDest.resolve('dist/znai').toString()]
}

scenario('shows help') {
    cli.run(unzippedZnai.bin) {
        output.should contain('--new')
        output.should contain('create new documentation with minimal')

        exitCode.should == 1
    }
}

scenario('scaffolds new documentation') {
    def scaffoldDir = fs.tempDir('znai-dist')
    def scaffoldCommand = "cd $scaffoldDir && ${unzippedZnai.bin} --new"

    cli.run(scaffoldCommand)

    def docRoot = scaffoldDir.resolve('znai')
    fs.textContent(docRoot.resolve('toc')).should contain('chapter-1one\n' +
            '    getting-started')
}

sscenario('sample serve') {
    def znaiDocs = '../../../../znai-docs/target'
    def serveCommand = "${unzippedZnai.bin} --serve --deploy=${znaiDocs}"

    def znaiServe = cli.backgroundCommand(serveCommand)
    znaiServe.start()

    sleep 2000
    znaiServe.stop()
}