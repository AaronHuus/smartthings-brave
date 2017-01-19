
/**
 * Copyright 2016-2017 SmartThings
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package smartthings.brave.mysql;


import com.mysql.jdbc.PreparedStatement;

public class DefaultQuerySpanNameProvider implements QuerySpanNameProvider {

  @Override
  public String getName(PreparedStatement statement) {
    return getName(statement.getPreparedSql());
  }

  @Override
  public String getName(String query) {
    if (query == null) {
      return null;
    }
    query = query.toLowerCase();
    if (query.equals("select 1")) {
      // TODO find a better way to ignore connection test statements
      return null;
    } else if (query.contains("@@session")) {
      // noop
    } else if (query.indexOf("select") == 0) {
      query = "select";
    } else if (query.indexOf("delete") == 0) {
      query = "delete";
    } else if (query.indexOf("update") == 0) {
      query = "update";
    } else if (query.indexOf("insert") == 0) {
      query = "insert";
    }

    return query.substring(0, Math.min(query.length(), 20)) + (query.length() > 20 ? "..." : "");
  }
}
