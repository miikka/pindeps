# pindeps

Verify the checksums of your dependency artifacts. Works with `deps.edn`.

This ensures that everyone is running the same code. This is both a correctness
and a safety measure.

Problems to be solved, features to be implemented:

* The bootstrapping problem: we must not accidentally load the dependencies
  from `deps.edn` before running this code. Possible solutions include creating
  a uberjar or a GraalVM native image.
* Can this code be hooked into tools.deps to automatically verify the
  dependencies?

## See also

* For Gradle, there's [gradle-witness](https://github.com/signalapp/gradle-witness).
* Go uses the `go.sum` files. They're verified against a [checksum database][go], similar to [Certificate Transparency][ct].
* Maven has some kind of[checksum support][mvn], but it only stores the checksums in the registry.
* npm's `package-lock.json` files include the `integrity` field. I'm not sure if npm actually verifies it.

[ct]: https://www.certificate-transparency.org/what-is-ct
[mvn]: https://books.sonatype.com/mvnref-book/reference/running-sect-options.html#running-sect-deps-option
[go]: https://go.googlesource.com/proposal/+/master/design/25530-sumdb.md
