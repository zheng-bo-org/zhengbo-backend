use std::fmt::Debug;
use crate::db::db::DB;
use crate::server::router::{Request, Router, RouterMethod, Response, ErrType};
use crate::server::router::RouterMethod::{
    GET,
    POST,
    DELETE
};
use serde::{
    Serialize,
    Deserialize
};

#[derive(Debug, Deserialize)]
pub struct BodyOfLoginValidation {

}

// fn is_login_expired(req: Request, db: &DB) -> Response {
//     // return Response{
//     //     body: Box::new(()),
//     //     err_type: ErrType::Success,
//     //     err_code: "".to_string(),
//     //     add_headers: Default::default()
//     // }
//
// }