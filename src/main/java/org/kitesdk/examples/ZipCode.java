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

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import org.apache.hadoop.io.Writable;

public class ZipCode implements Writable {
  public String _id = null;
  public String city = null;
  public List<Double> loc = null;
  public Long pop = null;
  public String state = null;

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(_id);
    out.writeUTF(city);
    out.writeDouble(loc.get(0));
    out.writeDouble(loc.get(1));
    out.writeLong(pop);
    out.writeUTF(state);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this._id = in.readUTF();
    this.city = in.readUTF();
    this.loc = Lists.newArrayList(in.readDouble(), in.readDouble());
    this.pop = in.readLong();
    this.state = in.readUTF();
  }
}
