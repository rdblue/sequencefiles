<!--
  - Copyright 2015 Cloudera Inc.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

## About

This repository contains a simple project used to demonstrate loading SequenceFile data into a Kite Dataset.

The example uses the [US zip code data][json-studio-data] from [JSON Studio][json-studio].

[json-studio]: http://jsonstudio.com/
[json-studio-data]: http://jsonstudio.com/resources/

## Creating a sample SequenceFile

First, create a Sequence file from the JSON data file, `zips.json` by running:

```
mvn package exec:java
```

You can edit the POM file to change the expected location of `zips.json`.

The command will create `zips.sequence`.

## Loading a SequenceFile using Kite

You can load a sample SequenceFile into a Kite dataset using the inputformat-import command. First, load your sequence file into HDFS:

```
hadoop fs -copyFromLocal zips.sequence
```

Next, build a Schema from the ZipCode class in `zips-1.jar`:

```
kite-dataset obj-schema org.kitesdk.examples.ZipCode --jar zips-1.jar -o zips.avsc
```

Create a dataset using that Schema:

```
kite-dataset create zips_from_seq --schema zips.avsc
```

Finally, load the data from `zips.sequence` in HDFS:

```
export HADOOP_CLASSPATH=zips-1.jar
kite-dataset inputformat-import hdfs:/user/cloudera/zips.sequence zips_from_seq \
          --format org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat \
          --jar zips-1.jar
```

**Notes**:

1. `HADOOP_CLASSPATH` is required until [CDK-918][] is fixed.
2. `SequenceFileInputFormat` reuses records, which is incompatible with loading data from the local FS until [CDK-898][] is fixed.

[CDK-918]: https://issues.cloudera.org/browse/CDK-918
[CDK-898]: https://issues.cloudera.org/browse/CDK-898
