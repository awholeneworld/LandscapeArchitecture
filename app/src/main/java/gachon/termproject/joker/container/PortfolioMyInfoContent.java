package gachon.termproject.joker.container;

public class PortfolioMyInfoContent {
    public Integer image;
    public String reviewStr;

    public PortfolioMyInfoContent() {

    }

    public PortfolioMyInfoContent(Integer image, String reviewStr) {
        this.image = image;
        this.reviewStr = reviewStr;
    }

    public Integer getImage() {
        return image;
    }
    public String getReviewStr() {
        return reviewStr;
    }

    public void setImage(Integer image) { this.image = image; }
    public void setReviewStr(String reviewStr) {
        this.reviewStr = reviewStr;
    }
}