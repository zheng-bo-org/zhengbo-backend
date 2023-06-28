#[derive(Debug)]
pub struct User {
    pub(crate) token: String,
    pub(crate) username: String,
    pub(crate) pwd: String
}

pub trait UserRouters {
    fn is_token_expired(self: &Self) -> bool;
}

impl UserRouters for User  {
    fn is_token_expired(self: &Self) -> bool {
        return self.token == "test"
    }
}