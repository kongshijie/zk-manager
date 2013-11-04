/**
 * 控制器
 * 
 * @author DJ
 */
var Control = function(config) {

	var config = config || {}
	var $this = this;

	var left = this.left = new ZooTree();
	var center = this.center = new ZooForm();
	left.getRootNode().expand(false);

	left.on("nodechanage", function(node) {
		       if(node.leaf) { //如果是子节点，则取得该节点的父节点
		       	      var parentNode = node.parentNode;
		       	     parentNode.reload();
		       	     parentNode.expand(true);
		       } else {
			       	node.reload();
					node.expand(true);
		       }
			}, this);

	/**
	 * 节点点击事件
	 */
	left.on("nodeclick", function(node) {
				center.items.items[0].setValue(node.attributes.data);
  				Ext.getCmp('curNodeLabel_1').setValue(node.attributes.path);
  				center.cacheCurNode = node;
    }, center);
    
    /**
     * 保存监听
     */
    center.on("saveEvent",function(node) {
	     if(node.leaf) { //如果是子节点，则取得该节点的父节点
	       	      var parentNode = node.parentNode;
	       	     parentNode.reload();
	       	     parentNode.expand(true);
	       } else {
		       	node.reload();
				node.expand(true);
	       }
    });
}
