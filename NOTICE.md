This project is an example of [Freestyle-Slick](http://frees.io/docs/integrations/slick/) integration with a Postgresql database.

## Generate

### Mapping

I'm using ìn this project [Slick-CodeGen](http://slick.lightbend.com/doc/3.2.0/codegen-api/index.html#package).

To generate the file `Tables.scala` with the mapping you can execute `sbt slick-gen`.

### Schema

To generate the schema you can run `Tables.schema.create` first in the main method.


***Note***: Set your own user and password in `application.conf` and `build.sbt`.

Copyright (c) 2017 47 Degrees.  All rights reserved.

Licensed under Apache License. See [LICENSE](LICENSE) for terms.