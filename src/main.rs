use crate::server::routers;
use crate::server::routers::user::User;
pub mod server;
pub mod db;

fn main() {
    let user: User = User {
        token: &"test".into(),
        username: &"test".into(),
        pwd: &"test".into(),
    };

    print!("{}", routers::user::UserRouters::is_token_expired(&user));
}