use axum::Router;

pub trait RouterLoader {
    fn load(self: &Self, router: Router) -> Router;
}