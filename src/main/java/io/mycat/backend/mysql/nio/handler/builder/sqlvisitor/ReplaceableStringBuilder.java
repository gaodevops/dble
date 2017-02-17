package io.mycat.backend.mysql.nio.handler.builder.sqlvisitor;

import java.util.ArrayList;
import java.util.List;

import io.mycat.plan.common.ptr.StringPtr;

/**
 * @author chenzifei
 * @CreateTime 2015年12月15日
 */
public class ReplaceableStringBuilder {
	private List<Element> elements;

	public ReplaceableStringBuilder() {
		elements = new ArrayList<Element>();
	}
	
	public Element getCurrentElement() {
		Element curEle = null;
		if (elements.isEmpty()) {
			curEle = new Element();
			elements.add(curEle);
		} else {
			curEle = elements.get(elements.size() - 1);
			if (curEle.getRepString() != null) {
				curEle = new Element();
				elements.add(curEle);
			}
		}
		return curEle;
	}

	public ReplaceableStringBuilder append(ReplaceableStringBuilder other) {
		if (other != null)
			this.elements.addAll(other.elements);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Element ele : elements) {
			sb.append(ele.getSb());
			StringPtr rep = ele.getRepString();
			if (rep != null)
				sb.append(rep.get());
		}
		return sb.toString();
	}

	public final static class Element {
		private final StringBuilder sb;
		private StringPtr repString;

		public Element() {
			sb = new StringBuilder();
		}

		/**
		 * @return the sb
		 */
		public StringBuilder getSb() {
			return sb;
		}

		/**
		 * @return the repString
		 */
		public StringPtr getRepString() {
			return repString;
		}

		/**
		 * @param repString
		 *            the repString to set
		 */
		public void setRepString(StringPtr repString) {
			if (this.repString != null)
				throw new RuntimeException("error use");
			this.repString = repString;
		}

	}

	/**
	 * like stringbuilder.setlength(0)
	 */
	public void clear() {
		elements.clear();
	}

}
