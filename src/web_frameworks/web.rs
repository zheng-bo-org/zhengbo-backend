use async_trait::async_trait;

#[async_trait]
pub trait Web {
    async fn start(self: &Self);
}