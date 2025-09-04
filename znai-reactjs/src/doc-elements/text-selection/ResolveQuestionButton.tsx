import React, { useState, useEffect, useRef } from 'react';
import './ResolveQuestionButton.css';

interface Props {
  onClick(): void;
}

export function ResolveQuestionButton({ onClick }: Props) {
  const [isConfirming, setIsConfirming] = useState(false);
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  const handleClick = () => {
    if (isConfirming) {
      onClick();
      setIsConfirming(false);
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
        timeoutRef.current = null;
      }
    } else {
      setIsConfirming(true);
      timeoutRef.current = setTimeout(() => {
        setIsConfirming(false);
        timeoutRef.current = null;
      }, 2000);
    }
  };

  return (
    <button 
      className={`resolve-question-button ${isConfirming ? 'confirming' : ''}`}
      onClick={handleClick}
    >
      {isConfirming ? 'Confirm' : 'Resolve'}
    </button>
  );
}
