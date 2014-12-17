package edu.fudan.weixin.utils;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;

public class AttributeRegexFilter extends HasAttributeFilter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2884276271929146321L;

	public AttributeRegexFilter(){
		super();
	}
	
	public AttributeRegexFilter(String attr, String value)
	{
		super(attr,value);
	}

	public boolean accept (Node node)
    {
        Tag tag;
        Attribute attribute;
        boolean ret;

        ret = false;
        if (node instanceof Tag)
        {
            tag = (Tag)node;
            attribute = tag.getAttributeEx (mAttribute);
            ret = null != attribute;
            if (ret && (null != mValue))
                ret =(attribute.getValue ()!=null) && (attribute.getValue ().matches(mValue));
        }

        return (ret);
    }
}
