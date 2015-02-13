/*
 * Copyright 2015 Cloudera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kitesdk.examples;

import java.io.File;
import java.io.FileInputStream;
import java.util.EnumSet;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: java -jar zips-1.jar <zips.json> <out.sequence>");
      System.exit(1);
    }

    SequenceFileInputFormat in;

    File file = new File(args[0]);
    if (!file.exists() || !file.canRead()) {
      System.err.println("Cannot read " + file);
    }

    Schema schema = ReflectData.get().getSchema(ZipCode.class);
    JSONFileReader<ZipCode> reader = new JSONFileReader<ZipCode>(
        new FileInputStream(file), schema, ZipCode.class);
    reader.initialize();

    FileContext context = FileContext.getLocalFSFileContext();
    SequenceFile.Writer writer = SequenceFile.createWriter(context,
        new Configuration(), new Path(args[1]),
        NullWritable.class, ZipCode.class,
        SequenceFile.CompressionType.NONE, null, new SequenceFile.Metadata(),
        EnumSet.of(CreateFlag.CREATE, CreateFlag.OVERWRITE));

    for (ZipCode zip : reader) {
      writer.append(NullWritable.get(), zip);
    }

    writer.close();
  }
}
