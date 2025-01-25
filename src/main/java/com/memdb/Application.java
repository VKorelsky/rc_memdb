package com.memdb;

import com.memdb.db.InMemoryDB;
import com.memdb.db.MemDB;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static void main(String[] args) {
        MemDB db = new InMemoryDB();
        DBController controller = new DBController(db);

        var app = Javalin.create()
                         .get("/get", controller::getValue)
                         .get("/set", controller::setValue)
                         .error(400, ctx -> ctx.result("bad request"));

        app.start(4000);
    }

    static class DBController {
        private final MemDB db;
        private final Logger logger = LoggerFactory.getLogger(DBController.class);

        DBController(MemDB db) {this.db = db;}

        private void getValue(final Context context) {
            String key = context.queryParam("key");

            if (key == null) {
                context.status(400);
                return;
            }

            logger.info("Fetching value for key: {}", key);

            var result = db.get(key)
                           .orElseGet(() -> {
                               logger.info("No value found for key {}, returning empty string", key);
                               return "";
                           });

            context.json(result);
        }

        private void setValue(final Context context) {
            String queryString = context.queryString();

            if (queryString == null) {
                context.status(400);
                return;
            }

            String[] params = queryString.split("=");

            if (!(params.length == 2)) {
                context.status(400);
                return;
            }

            String key = params[0], value = params[1];

            logger.info("Setting key = {}, value = {}", key, value);
            var result = db.set(key, value);

            context.json(result);
        }
    }
}
