use std::alloc::System;
use std::collections::HashMap;
use std::fmt::Debug;
use std::time::SystemTime;
use crate::db::db::DB;
use serde::{
    Serialize,
    Deserialize,
};
use crate::server::router_loader::RouterLoader;
use axum::{
    Router,
    extract::{
        Query,
        Path,
    },
    Json,
};
use axum::http::StatusCode;
use axum::response::IntoResponse;
use axum::routing::get;
use log::info;
use serde_json::{json, Value};


pub struct User {}

async fn refresh_token(Query(param): Query<HashMap<String, String>>) -> Result<Json<Value>, (StatusCode, Json<Value>)> {
    let old_token = match param.get("token") {
        Some(token) => token,
        None => {
            let err_body = json!({
               "message": "Token is required."
            });
            return Err((StatusCode::BAD_REQUEST, Json(err_body)));
        }
    };

    Ok(Json(json!({
         "token": old_token
     })))
}

impl RouterLoader for User {
    fn load(self: &Self, router: Router) -> Router {
        return router.route("/users/token", get(refresh_token));
    }
}