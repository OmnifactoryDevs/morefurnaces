package dan.morefurnaces.lib;

public class ControlData {
    private Integer width = null;
    private Integer height = null;

    private Integer left = null;
    private Integer right = null;
    private Integer top = null;
    private Integer bottom = null;

    private String cleft = null; // null is parent
    private String cright = null; // null is parent
    private String ctop = null; // null is parent
    private String cbottom = null; // null is parent

    private Boolean sameSideLeft = null;
    private Boolean sameSideRight = null;
    private Boolean sameSideTop = null;
    private Boolean sameSideBottom = null;

    /**
     * Overrides left and right. Sets both to 0
     */
    private Boolean centerHor = null;
    /**
     * Overrides top and bottom. Sets both to 0
     */
    private Boolean centerVert = null;

    /**
     * Overrides centerHor and centerVert. Sets both to true
     */
    private Boolean center = null;

    public void apply(Anchor anchor, ControlContainer parent, IControlProvider provider) {
        if (center != null && center)
            centerHor = centerVert = true;

        if (centerHor != null && centerHor)
            left = right = 0;
        if (centerVert != null && centerVert)
            top = bottom = 0;

        if (width != null)
            anchor.width(width);
        if (height != null)
            anchor.height(height);

        if (left != null) {
            Control c = cleft == null ? parent : provider.getControl(cleft);
            anchor.left(c, left, c == parent || (sameSideLeft != null && sameSideLeft));
        }

        if (right != null) {
            Control c = cright == null ? parent : provider.getControl(cright);
            anchor.right(c, right, c == parent || (sameSideRight != null && sameSideRight));
        }

        if (top != null) {
            Control c = ctop == null ? parent : provider.getControl(ctop);
            anchor.top(c, top, c == parent || (sameSideTop != null && sameSideTop));
        }

        if (bottom != null) {
            Control c = cbottom == null ? parent : provider.getControl(cbottom);
            anchor.bottom(c, bottom, c == parent || (sameSideBottom != null && sameSideBottom));
        }
    }

    void claimChildValues(ControlData data) {
        if (data.width != null)
            width = data.width;
        if (data.height != null)
            height = data.height;
        if (data.left != null)
            left = data.left;
        if (data.right != null)
            right = data.right;
        if (data.top != null)
            top = data.top;
        if (data.bottom != null)
            bottom = data.bottom;

        if (data.center != null)
            center = data.center;
        if (data.centerHor != null)
            centerHor = data.centerHor;
        if (data.centerVert != null)
            centerVert = data.centerVert;

        if (data.sameSideLeft != null)
            sameSideLeft = data.sameSideLeft;
        if (data.sameSideRight != null)
            sameSideRight = data.sameSideRight;
        if (data.sameSideTop != null)
            sameSideTop = data.sameSideTop;
        if (data.sameSideBottom != null)
            sameSideBottom = data.sameSideBottom;
    }

    @Override
    protected ControlData clone() {
        ControlData data = new ControlData();
        data.width = width;
        data.height = height;
        data.left = left;
        data.right = right;
        data.top = top;
        data.bottom = bottom;
        data.cleft = cleft;
        data.cright = cright;
        data.ctop = ctop;
        data.cbottom = cbottom;
        data.center = center;
        data.centerVert = centerVert;
        data.centerHor = centerHor;
        data.sameSideLeft = sameSideLeft;
        data.sameSideRight = sameSideRight;
        data.sameSideTop = sameSideTop;
        data.sameSideBottom = sameSideBottom;
        return data;
    }
}
